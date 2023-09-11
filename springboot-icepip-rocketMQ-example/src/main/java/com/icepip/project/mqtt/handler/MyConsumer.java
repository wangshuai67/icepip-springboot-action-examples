package com.icepip.project.mqtt.handler;

import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Service;

/**
 * 定义一个消费者，监听test-topic主题的消息
 * @author 冰点
 * @version 1.0.0
 * @date 2023/9/9 16:29
 */

@Service
@RocketMQMessageListener(topic = "test-topic", consumerGroup = "my-consumer_test-topic")
public class MyConsumer implements RocketMQListener<String>{

    // 当收到消息时，该方法将被调用
    @Override
    public void onMessage(String message) {
        System.out.println("Received message: "+ message);
    }
}
