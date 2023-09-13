package com.icepip.project.mqtt.config;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * MQTT配置
 *
 * @author 冰点
 * @version 1.0.0
 * @date 2023/9/8 16:14
 */

@Configuration
public class MqttConfig {

    @Value("${mqtt.broker}")
    private String mqttBroker;

    @Value("${mqtt.clientId}")
    private String mqttClientId;
    @Value("${mqtt.username}")
    private String username;
    @Value("${mqtt.password}")
    private String password;
    @Value("${mqtt.cleanSession}")
    private Boolean cleanSession;

    @Value("${mqtt.keepAlive}")
    private Integer keepAlive;

    @Value("${mqtt.timeout}")
    private Integer timeout;

    @Bean
    public MqttConnectOptions mqttConnectOptions() {
        MqttConnectOptions options = new MqttConnectOptions();
        options.setServerURIs(new String[]{mqttBroker});
        options.setCleanSession(true);
        // 其他设置，如用户名和密码等
        options.setUserName(username);
        options.setPassword(password.toCharArray());
        options.setCleanSession(cleanSession);
        options.setKeepAliveInterval(keepAlive);
        options.setConnectionTimeout(timeout);
        return options;
    }

    @Bean
    public MqttClient mqttClient() throws MqttException {
        MqttClient mqttClient = new MqttClient(mqttBroker, MqttClient.generateClientId());
        mqttClient.connect(mqttConnectOptions());
        return mqttClient;
    }


}