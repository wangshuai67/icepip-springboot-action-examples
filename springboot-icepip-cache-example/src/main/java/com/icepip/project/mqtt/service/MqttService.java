package com.icepip.project.mqtt.service;

import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
/**
 * @author 冰点(icepip.blog.csdn.com)
 * @version 1.0.0
 * @date 2023/9/8 16:15
 */
@Service
public class MqttService {

    @Autowired
    private MqttClient mqttClient;


    public void publishMessage(String topic, String message) throws MqttException {
        MqttMessage mqttMessage = new MqttMessage(message.getBytes());
        mqttClient.publish(topic, mqttMessage);
    }

    public void subscribeToTopic(String topic, IMqttMessageListener listener) throws MqttException {
        mqttClient.subscribe(topic, listener);
    }
}