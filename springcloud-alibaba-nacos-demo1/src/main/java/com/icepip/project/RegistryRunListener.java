package com.icepip.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * @author xiaojing
 */

//@Component
public class RegistryRunListener implements SpringApplicationRunListener {

    private final SpringApplication application;

    private final String[] args;

    public RegistryRunListener(SpringApplication application, String[] args) {
        this.application = application;
        this.args = args;
    }

    public void starting() {
    }

    public void environmentPrepared(ConfigurableEnvironment configurableEnvironment) {

    }

    public void contextPrepared(ConfigurableApplicationContext configurableApplicationContext) {
    }

    public void contextLoaded(ConfigurableApplicationContext configurableApplicationContext) {
    }

    public void finished(ConfigurableApplicationContext configurableApplicationContext, Throwable throwable) {
        invokeRegisteryExclude(configurableApplicationContext);
    }

    public void started(ConfigurableApplicationContext context) {
        invokeRegisteryExclude(context);
    }

    public void running(ConfigurableApplicationContext context) {

    }

    public void failed(ConfigurableApplicationContext context, Throwable exception) {

    }

    private void invokeRegisteryExclude(ConfigurableApplicationContext context) {
        RegistrySwitcher registrySwitcher = null;

        try {
            registrySwitcher = context.getBean(RegistrySwitcher.class);
        } catch (Exception ignore) {

        }

        if (null != registrySwitcher) {
            String[] excludeRegisters = context.getEnvironment().getProperty(
                "spring.cloud.edas.migration.registry.excludes", "").toUpperCase().split(",");
            registrySwitcher.doUnregister(excludeRegisters);
        }
    }
}
