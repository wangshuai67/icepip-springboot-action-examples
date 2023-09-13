package com.icepip.project.mqtt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 使用Spring WebSocket API和JSR 356 Java WebSocket API两种方式实现WebSocket
 * 详细解释请参见我的博客
 * @author 冰点
 * @date 2023-9-1 14:55:18
 * @version 1.0
 */
@SpringBootApplication
public class SpringbootIcepipMqttApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootIcepipMqttApplication.class, args);
	}

}
