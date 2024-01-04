package com.icepip.project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.cloud.client.serviceregistry.ServiceRegistry;
import org.springframework.cloud.context.scope.refresh.RefreshScopeRefreshedEvent;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import javax.annotation.PostConstruct;
import java.lang.reflect.Field;
import java.util.*;

/**
 * @author xiaojing
 */
//@Configuration
public class RegistrySwitcher {

    @Autowired
    ConfigurableApplicationContext context;

    @Autowired
    private List<ServiceRegistry> serviceRegistryList;

    @Autowired
    private List<Registration> registrations;

    @Value("${spring.cloud.edas.migration.registry.excludes:}")
    private String excludeRegisters;
    private Set<String> unRegisteredRegistries = new HashSet<>();
    private HashMap<String, Registration> registrationHashMap = new HashMap<>();
    private HashMap<String, ServiceRegistry> serviceRegistryHashMap = new HashMap<>();
    private List<String> KNOWN_REGISTRY_TYPES = Arrays.asList("NACOS", "EUREKA", "CONSUL", "ZOOKEEPER");

    @PostConstruct
    public void init() {

        for (ServiceRegistry registry : serviceRegistryList) {
            for (String type : KNOWN_REGISTRY_TYPES) {
                if (registry.getClass().getName().toUpperCase().contains(type)) {
                    serviceRegistryHashMap.put(type, registry);
                }
            }
        }

        for (Registration registration : registrations) {
            for (String type : KNOWN_REGISTRY_TYPES) {
                if (registration.getClass().getName().toUpperCase().contains(type)) {
                    registrationHashMap.put(type, registration);
                }
            }
        }

    }

    @EventListener(RefreshScopeRefreshedEvent.class)
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public void onApplicationEvent(RefreshScopeRefreshedEvent event) {

        String preExcludeRegister = excludeRegisters;

        //这里已经可以拿到最新的配置信息了
        excludeRegisters = context.getEnvironment().getProperty(
            "spring.cloud.edas.migration.registry.excludes");

        if (!excludeRegisters.equalsIgnoreCase(preExcludeRegister)) {

            String[] excludes = excludeRegisters.toUpperCase().split(",");
            doRegister(excludes);
            doUnregister(excludes);
        }

    }

    public void doRegister(String[] excludes) {
        for (String type : KNOWN_REGISTRY_TYPES) {
            registerSelectedRegistry(excludes, type);
        }
    }

    public void doUnregister(String[] excludes) {

        for (String type : KNOWN_REGISTRY_TYPES) {
            unRegisterSelectedRegistry(excludes, type);
        }
    }

    private void unRegisterSelectedRegistry(String[] excludes, String type) {

        if (!Arrays.asList(excludes).contains(type)) {
            return;
        }

        Registration registration = registrationHashMap.get(type);
        ServiceRegistry serviceRegistry = serviceRegistryHashMap.get(type);

        if (null != registration && null != serviceRegistry) {
            serviceRegistry.deregister(registration);
            unRegisteredRegistries.add(type);

            //eureka 比较特别，在 refresh event 出现的时候，会自动先注销再注册，需要特别处理
            if (type.equals("EUREKA")) {
                disableEurekaRefreshEvent();
            }
        }

    }

    private void registerSelectedRegistry(String[] excludes, String type) {

        if (Arrays.asList(excludes).contains(type)) {
            return;
        }

        if (!unRegisteredRegistries.contains(type)) {
            return;
        }

        Registration registration = registrationHashMap.get(type);
        ServiceRegistry serviceRegistry = serviceRegistryHashMap.get(type);

        if (null != registration && null != serviceRegistry) {
            serviceRegistry.register(registration);
            unRegisteredRegistries.remove(type);

            //eureka 比较特别，在 refresh event 出现的时候，会自动先注销再注册，需要特别处理
            if (type.equals("EUREKA")) {
                enableEurekaRefreshEvent();
            }
        }

    }

    private void disableEurekaRefreshEvent() {
        try {

            // 将 EurekaClientConfigurationRefresher 修改，以防再次注册
            Object eurekaRefresher = context.getBean(
                "org.springframework.cloud.netflix.eureka"
                    + ".EurekaDiscoveryClientConfiguration$EurekaClientConfigurationRefresher");

            Class clazz = Class.forName(
                "org.springframework.cloud.netflix.eureka"
                    + ".EurekaDiscoveryClientConfiguration$EurekaClientConfigurationRefresher");

            Field autoRegistrationField = clazz.getDeclaredField("autoRegistration");
            autoRegistrationField.setAccessible(true);
            autoRegistrationField.set(eurekaRefresher, null);

            Field eurekaClientField = clazz.getDeclaredField("eurekaClient");
            eurekaClientField.setAccessible(true);
            eurekaClientField.set(eurekaRefresher, null);
        } catch (Exception ignore) {
        }
    }

    private void enableEurekaRefreshEvent() {
        try {

            // 将 EurekaClientConfigurationRefresher 修改，使得其在出现 RefreshEvent 的时候，还能够重新注册
            Object eurekaRefresher = context.getBean(
                "org.springframework.cloud.netflix.eureka"
                    + ".EurekaDiscoveryClientConfiguration$EurekaClientConfigurationRefresher");

            Class clazz = Class.forName(
                "org.springframework.cloud.netflix.eureka"
                    + ".EurekaDiscoveryClientConfiguration$EurekaClientConfigurationRefresher");

            Class eurekaClientClass = Class.forName("com.netflix.discovery.EurekaClient");
            Object eurekaClient = context.getBean(eurekaClientClass);
            Field eurekaClientField = clazz.getDeclaredField("eurekaClient");
            eurekaClientField.setAccessible(true);
            eurekaClientField.set(eurekaRefresher, eurekaClient);

            Class eurekaAutoServiceRegistrationClass = Class.forName(
                "org.springframework.cloud.netflix.eureka.serviceregistry.EurekaAutoServiceRegistration");
            Object autoRegistration = context.getBean(eurekaAutoServiceRegistrationClass);
            Field autoRegistrationField = clazz.getDeclaredField("autoRegistration");
            autoRegistrationField.setAccessible(true);
            autoRegistrationField.set(eurekaRefresher, autoRegistration);

        } catch (Exception ignore) {
        }
    }
}
