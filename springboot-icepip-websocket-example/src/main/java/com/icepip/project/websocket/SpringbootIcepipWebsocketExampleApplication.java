package com.icepip.project.websocket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;

/**
 * 使用Spring WebSocket API和JSR 356 Java WebSocket API两种方式实现WebSocket
 * 详细解释请参见我的博客
 * @author 冰点
 * @date 2023-9-1 14:55:18
 * @version 1.0
 */
@EnableWebSocket
@SpringBootApplication
public class SpringbootIcepipWebsocketExampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootIcepipWebsocketExampleApplication.class, args);
	}

}
