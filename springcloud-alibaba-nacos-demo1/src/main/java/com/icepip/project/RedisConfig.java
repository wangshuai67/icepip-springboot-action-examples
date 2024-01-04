package com.icepip.project;

import com.alibaba.nacos.common.utils.StringUtils;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.data.redis.connection.*;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 支持动态配置Redis 多数据源，无需在代码中硬编码多数据源的配置项
 * @author 冰点
 * @version 1.0.0
 * @date 2023/12/29 15:12
 */

@Configuration
@ConfigurationProperties(prefix = "spring")
public class RedisConfig implements ApplicationContextAware, InitializingBean, BeanPostProcessor {
    /**
     * 将多个Redis 数据源的配置信息放到一个Map中,然后遍历Map 创建不同的RedisTemplate实例
     * Map<String, Map<String, Object>> redis 中的 key 为配置的spring.redis.xx  xx为key  value xx一组的值组装成了map
     * 创建的RedisTemplate实例的名称为 xxRedisTemplate
     */
    protected static Map<String, Map<String, Object>> redis = new HashMap<>();
    // 配置主数据源
    @Value("${primary.redis.key:master}")
    private String primaryKey;
    private static ApplicationContext applicationContext;

    public RedisConfig() {
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        RedisConfig.applicationContext = applicationContext;
    }

    @Override
    public void afterPropertiesSet() {
        redis.forEach((k, v) -> {
            if (Objects.equals(k, this.primaryKey)) {
                Map<String, Object> paramMap = new HashMap(4);
                v.forEach((k1, v1) -> {
                    paramMap.put("spring.redis." + k1, v1);
                });
                MapPropertySource mapPropertySource = new MapPropertySource("redisAutoConfigProperty", paramMap);
                ((StandardEnvironment) applicationContext.getEnvironment()).getPropertySources().addLast(mapPropertySource);
            }
            RedisConnectionFactory lettuceConnectionFactory = this.buildLettuceConnectionFactory(k, v, this.buildGenericObjectPoolConfig(k, v));
            this.buildRedisTemplate(k, lettuceConnectionFactory);
            this.buildStringRedisTemplate(k, lettuceConnectionFactory);
        });
    }


    @Bean
    public RedisTemplate<Object, Object> redisTemplate() {
        Map<String, Object> redisParam = redis.get(this.primaryKey);
        GenericObjectPoolConfig<?> genericObjectPoolConfig = this.buildGenericObjectPoolConfig(this.primaryKey, redisParam);
        RedisConnectionFactory lettuceConnectionFactory = this.buildLettuceConnectionFactory(this.primaryKey, redisParam, genericObjectPoolConfig);
        RedisTemplate<Object, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(lettuceConnectionFactory);
        return template;
    }

    @Bean
    @ConditionalOnMissingBean
    public StringRedisTemplate stringRedisTemplate() {
        Map<String, Object> redisParam = redis.get(this.primaryKey);
        GenericObjectPoolConfig<?> genericObjectPoolConfig = this.buildGenericObjectPoolConfig(this.primaryKey, redisParam);
        RedisConnectionFactory lettuceConnectionFactory = this.buildLettuceConnectionFactory(this.primaryKey, redisParam, genericObjectPoolConfig);
        StringRedisTemplate template = new StringRedisTemplate();
        template.setConnectionFactory(lettuceConnectionFactory);
        return template;
    }


    private void buildStringRedisTemplate(String k, RedisConnectionFactory lettuceConnectionFactory) {
        ConstructorArgumentValues constructorArgumentValues = new ConstructorArgumentValues();
        constructorArgumentValues.addIndexedArgumentValue(0, lettuceConnectionFactory);
        this.setCosBean(k + "StringRedisTemplate", StringRedisTemplate.class, constructorArgumentValues);
    }

    private void buildRedisTemplate(String k, RedisConnectionFactory lettuceConnectionFactory) {
        Jackson2JsonRedisSerializer<?> serializer = new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        serializer.setObjectMapper(mapper);
        Map<String, Object> original = new HashMap<>(2);
        original.put("connectionFactory", lettuceConnectionFactory);
        original.put("valueSerializer", serializer);
        original.put("keySerializer", new StringRedisSerializer());
        original.put("hashKeySerializer", new StringRedisSerializer());
        original.put("hashValueSerializer", serializer);
        this.setBean(k + "RedisTemplate", RedisTemplate.class, original);
    }

    public GenericObjectPoolConfig<?> buildGenericObjectPoolConfig(String redisKey, Map<String, Object> param) {
        GenericObjectPoolConfig<?> result;
        if (applicationContext.containsBean(redisKey + "GenericObjectPoolConfig")) {
            result = getBean(redisKey + "GenericObjectPoolConfig");
        } else {
            Map<String, Object> original = new HashMap<>(8);
            original.put("maxTotal", param.getOrDefault("maxTotal", 8));
            original.put("maxIdle", param.getOrDefault("maxIdle", 8));
            original.put("minIdle", param.getOrDefault("minIdle", 0));
            original.put("maxWaitMillis", param.getOrDefault("maxWaitMillis", -1L));
            original.put("testOnBorrow", param.getOrDefault("testOnBorrow", Boolean.FALSE));
            this.setBean(redisKey + "GenericObjectPoolConfig", GenericObjectPoolConfig.class, original);
            result = getBean(redisKey + "GenericObjectPoolConfig");
        }


        return result;
    }

    public RedisConnectionFactory buildLettuceConnectionFactory(String redisKey, Map<String, Object> param, GenericObjectPoolConfig genericObjectPoolConfig) {
        if (!applicationContext.containsBean(redisKey + "redisConnectionFactory")) {
            long timeout = Long.parseLong((String) param.getOrDefault("timeout", "3000"));
            LettuceClientConfiguration clientConfig = LettucePoolingClientConfiguration.builder().commandTimeout(Duration.ofMillis(timeout)).poolConfig(genericObjectPoolConfig).build();
            RedisConfiguration firstArgument = null;
            // 根据配置项判断是否是集群或者哨兵或者单机
            if (this.isCluster(param)) {
                firstArgument = this.buildClusterConfig(param);
            } else if (this.isSentinel(param)) {
                firstArgument = this.buildSentinelConfig(param);
            } else {
                firstArgument = this.buildStandaloneConfig(param);
            }

            ConstructorArgumentValues constructorArgumentValues = new ConstructorArgumentValues();
            constructorArgumentValues.addIndexedArgumentValue(0, firstArgument);
            constructorArgumentValues.addIndexedArgumentValue(1, clientConfig);
            this.setCosBean(redisKey + "redisConnectionFactory", LettuceConnectionFactory.class, constructorArgumentValues);
        }
        return getBean(redisKey + "redisConnectionFactory");
    }


    private RedisStandaloneConfiguration buildStandaloneConfig(Map<String, Object> param) {
        RedisStandaloneConfiguration standaloneConfig = new RedisStandaloneConfiguration();
        standaloneConfig.setHostName(String.valueOf(param.get("host")));
        standaloneConfig.setDatabase(Integer.parseInt((String) param.get("database")));
        standaloneConfig.setPort(Integer.parseInt((String) param.get("port")));
        standaloneConfig.setPassword(RedisPassword.of((String) param.get("password")));
        return standaloneConfig;
    }

    private RedisSentinelConfiguration buildSentinelConfig(Map<String, Object> param) {
        RedisSentinelConfiguration config = new RedisSentinelConfiguration();
        // todo 如果是哨兵模式需要要在此处额外的配置，可以完善
        return config;
    }

    private RedisClusterConfiguration buildClusterConfig(Map<String, Object> param) {
        RedisClusterConfiguration config = new RedisClusterConfiguration();
        // todo 如果是Cluster模式需要在此处额外的配置，可以完善
        return config;
    }


    private static void checkApplicationContext() {
        if (applicationContext == null) {
            throw new IllegalStateException("applicaitonContext未注入,请在applicationContext.xml中定义SpringContextUtil");
        }
    }

    public static <T> T getBean(String name) {
        checkApplicationContext();
        return applicationContext.containsBean(name) ? (T) applicationContext.getBean(name) : null;
    }


    public synchronized void setBean(String beanName, Class<?> clazz, Map<String, Object> original) {
        checkApplicationContext();
        DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) applicationContext.getAutowireCapableBeanFactory();
        if (!beanFactory.containsBean(beanName)) {
            GenericBeanDefinition definition = new GenericBeanDefinition();
            definition.setBeanClass(clazz);
            if (beanName.startsWith(this.primaryKey)) {
                definition.setPrimary(true);
            }
            definition.setPropertyValues(new MutablePropertyValues(original));
            beanFactory.registerBeanDefinition(beanName, definition);
        }
    }

    public synchronized void setCosBean(String beanName, Class<?> clazz, ConstructorArgumentValues original) {
        checkApplicationContext();
        DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) applicationContext.getAutowireCapableBeanFactory();
        if (!beanFactory.containsBean(beanName)) {
            GenericBeanDefinition definition = new GenericBeanDefinition();
            definition.setBeanClass(clazz);
            if (beanName.startsWith(this.primaryKey)) {
                definition.setPrimary(true);
            }

            definition.setConstructorArgumentValues(new ConstructorArgumentValues(original));
            beanFactory.registerBeanDefinition(beanName, definition);
        }
    }

    private boolean isSentinel(Map<String, Object> param) {
        String sentinelMaster = (String) param.get("sentinel.master");
        String sentinelNodes = (String) param.get("sentinel.nodes");
        return StringUtils.isNotEmpty(sentinelMaster) && StringUtils.isNotEmpty(sentinelNodes);
    }

    private boolean isCluster(Map<String, Object> param) {
        String clusterNodes = (String) param.get("cluster.nodes");
        return StringUtils.isNotEmpty(clusterNodes);
    }


    public Map<String, Map<String, Object>> getRedis() {
        return redis;
    }
}
