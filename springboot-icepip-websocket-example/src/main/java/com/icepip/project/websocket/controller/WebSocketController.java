package com.icepip.project.websocket.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
/**
 * 第二种方式 使用Spring WebSocket API
 * 实现消息接收与发送  详细解释请参见我的博客
 * 客户端的实现在这个文件：springboot-icepip-websocket-example\src\main\resources\static\stompWebsocketDemo.html
 * @author 冰点
 * @version 1.0.0
 * @date 2023/9/1 14:46
 */

@Slf4j
@Controller
public class WebSocketController {
    /**
     * ·@MessageMapping spring WebSocket的注解，用于映射WebSocket消息到特定的方法。
     *
     * ·@SendTo("/topic/greetings")：这也是Spring WebSocket的注解，用于指定方法返回的消息应该发送到哪个目的地。
     * @param message 返回的消息，将发送给客户端订阅/topic/greetings 的处理方法 stompWebsocketDemo.html
     * @return
     */
    @MessageMapping("/websocketDemo2")
    @SendTo("/topic/greetings")
    public String handleHelloMessage(String message) {
        log.info("接收到消息:{}",message);
        return "Hello, " + message + "!";
    }
}