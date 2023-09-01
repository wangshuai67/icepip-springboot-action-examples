package com.icepip.project.websocket.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

 /**
  * 第一种方式 使用JSR 356 Java WebSocket API
  * 也可以不要这个配置类，直接在启动类上加上@EnableWebSocket即可
  * 为了解释专门写了这个类
  * 简单来说，ServerEndpointExporter的作用是
  * 1. 自动扫描并注册所有带有@ServerEndpoint注解的Bean。
  * 2. 为每个WebSocket endpoint提供一个独立的ServerContainer。
  * 如果你引入了spring-boot-starter-websocket依赖，并且你的应用是独立的Spring Boot应用（即不是部署到外部的Servlet容器中）
  * ，那么这个Bean会被自动创建，你不需要额外定义它。如果你的应用是部署到外部的Servlet容器中，那么你需要手动定义这个Bean。
  * @author 冰点
  * @version 1.0.0
  * @date 2023/9/1 11:50
  */


@Configuration
public class WebSocketConfig {

    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }
}
