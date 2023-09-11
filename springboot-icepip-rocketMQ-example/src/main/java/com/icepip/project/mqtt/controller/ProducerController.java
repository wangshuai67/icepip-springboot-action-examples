package com.icepip.project.mqtt.controller;

import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
/**
 *  SpringBoot集成Apache RocketMQ详解
 * @author 冰点
 * @version 1.0.0
 * @date 2023/9/9 17:02
 */

@RestController
@RequestMapping("/producer")
public class ProducerController {

    @Autowired
    private RocketMQTemplate rocketMQTemplate;


    /**
     * 同步发送消息到指定主题
     * @param message
     * @return
     */
    @GetMapping("/syncSend")
    public String syncSend(String message) {
        // 同步发送消息到指定主题
        rocketMQTemplate.syncSend("test-topic", message);
        return "Sync message: " + message + " sent";
    }
    /**
     * 异步发送消息到指定主题
     * @param message
     * @return
     */
    @GetMapping("/asyncSend")
    public String asyncSend(String message) {
        // 异步发送消息到指定主题
        rocketMQTemplate.asyncSend("test-topic", MessageBuilder.withPayload(message).build(), new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                System.out.println("Async message sent successfully, result: " + sendResult);
            }

            @Override
            public void onException(Throwable throwable) {
                System.err.println("Failed to send async message: " + throwable.getMessage());
            }
        }, 3000, 3); // 3000 ms timeout, delay level 3

        return "Async message: " + message + " sent";
    }

    /**
     * 发送单向消息到指定主题，无需等待Broker的确认
     * @param message
     * @return
     */
    @GetMapping("/sendOneWay")
    public String sendOneWay(String message) {
        // 发送单向消息到指定主题，无需等待Broker的确认
        rocketMQTemplate.sendOneWay("test-topic", message);
        return "OneWay message: " + message + " sent";
    }

    // 发送顺序消息
    @GetMapping("/sendOrderly")
    public String sendOrderly(String message) {
        // 发送顺序消息到指定主题
        rocketMQTemplate.syncSendOrderly("test-topic", message, "order");
        return "Orderly message: " + message + " sent";
    }

    // 发送延迟消息
    @GetMapping("/sendDelayed")
    public String sendDelayed(String message) {
        // 发送延迟消息到指定主题，延迟级别为3
        rocketMQTemplate.syncSend("test-topic", MessageBuilder.withPayload(message).build(), 1000, 3);
        return "Delayed message: " + message + " sent";
    }

    // 发送批量消息
    @GetMapping("/sendBatch")
    public String sendBatch() {
        List<String> messages = new ArrayList<>();
        messages.add("message1");
        messages.add("message2");
        // 批量发送消息到指定主题
        rocketMQTemplate.syncSend("test-topic", messages);
        return "Batch messages sent";
    }
}
