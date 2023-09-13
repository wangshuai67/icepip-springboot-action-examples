package com.icepip.project.mqtt.controller;

import com.icepip.project.mqtt.service.MqttService;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

/**
 * 使用Controller来直观的测试验证
 * @author 冰点(icepip.blog.csdn.com)
 * @version 1.0.0
 * @date 2023/9/8 16:15
 */

@RestController
@RequestMapping("/mqtt")
public class MqttController {

    private final MqttService mqttService;
    @Value("${mqtt.topic:testTopic}")
    public String DEFAULT_TOPIC;

    @Autowired
    public MqttController(MqttService mqttService) {
        this.mqttService = mqttService;
    }

    /**
     * 模拟发布消息
     *
     * @param topic
     * @param message
     * @throws MqttException
     */
    @PostMapping("/publish")
    public void publishMessage(@RequestParam String topic, @RequestParam String message) throws MqttException {
        if (StringUtils.isEmpty(topic)) {
            topic=this.DEFAULT_TOPIC;
        }
        mqttService.publishMessage(topic, message);
    }

    /**
     * 模拟订阅消息
     *
     * @param topic
     * @throws MqttException
     */
    @PostMapping("/subscribe")
    public void subscribeToTopic(@RequestParam String topic) throws MqttException {
        mqttService.subscribeToTopic(topic, (topic1, message) ->
                System.out.println("Received topic:"+topic1+",message: " + new String(message.getPayload())));
    }

    /**
     * 监听默认消息
     * @throws MqttException
     */
    @PostConstruct
    public void subscribeToTopic() throws MqttException {
        mqttService.subscribeToTopic(DEFAULT_TOPIC, (topic1, message) ->
                System.out.println("Received topic:"+topic1+",message: " + new String(message.getPayload())));
    }
}