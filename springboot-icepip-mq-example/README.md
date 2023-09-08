## Spring boot 集成MQTT示例
# 简介
MQTT，全称Message Queuing Telemetry Transport，即消息队列遥测传输。它是一种基于发布/订阅模式的"轻量级"消息协议，有助于低带宽、不可靠或高延迟的网络环境中的远程传感器和控制设备消息通信。

让我们以一个生动的例子来揭开MQTT的神秘面纱。假设你是一名热衷于植物种植的园丁，你有一个自动灌溉系统，它可以根据植物的需要，如土壤湿度、天气预报等，自动浇水。在这个系统中，MQTT就像是你和这个自动灌溉系统之间的通信者。

- **发布/订阅模式：** 这就像你告诉自动灌溉系统：“嘿，如果土壤湿度低于某个阈值，就告诉我一声。”（即订阅），然后，当土壤湿度确实低于阈值时，系统就会通知你：“嘿，土壤湿度低了，需要浇水了。”（即发布）。

- **轻量级：** MQTT设计得非常简洁轻巧，就像是你和自动灌溉系统的对话中只包含必要的信息，例如“土壤湿度”、“浇水”，而不包含任何复杂的语法或者冗余的信息。

- **QoS（Quality of Service）：** MQTT定义了三种消息质量等级：QoS 0（最多一次）、QoS 1（至少一次）和QoS 2（只有一次）。这就像是你可以告诉自动灌溉系统：“对于浇水这件事，我希望你只通知我一次。”或者“对于土壤湿度的问题，我希望你一直通知我，直到我处理完为止。”

- **保留消息和遗嘱消息：** MQTT可以设置保留消息，让新订阅者立即得到最新的更新。此外，还可以设置遗嘱消息，当客户端异常断开连接时，服务器会发布这个遗嘱消息，通知其他客户端。这就像是你可以对自动灌溉系统说：“如果我突然失去联系，你就自动启动浇水程序。”

 # 搭建MQTT 服务端

我是用docker 搭建了一个简单 的mosquitto 服务端

先创建映射目录
```bash
  $ mkdir -p /data/mosquitto/config 
  $ mkdir -p  /data/mosquitto/data 
  $ mkdir -p  /data/mosquitto/log
```

```bash
root@ip /data/mosquitto/config# ll
total 4
-rw-r--r-- 1 1883 1883 168 Sep  8 18:05 mosquitto.conf
-rw-r--r-- 1 root root   0 Sep  8 18:02 passwd
```
## mosquitto.conf
新建 `/data/mosquitto/config/mosquitto.conf`
 追加一下内容
```bash

persistence true
persistence_location /mosquitto/data/
allow_anonymous false
log_dest file /mosquitto/log/mosquitto.log
listener 1883

```
```bash
docker run -it --name=mosquitto --privileged -p 1883:1883 -p 9003:9001 -v /data/mosquitto/config/mosquitto.conf:/mosquitto/config/mosquitto.conf -v /data/mosquitto/data:/mosquitto/data -v /data/mosquitto/log:/mosquitto/log -d eclipse-mosquitto
```
```sql
# 进入容器执行
$ docker exec -it b495e3d42429 sh
# 容器内执行
$ mosquitto_passwd -c /data/mosquitto/passwd root
输入两次密码后

```

### 修改配置文件，追加密码文件
 mosquitto.conf
```bash

persistence true
persistence_location /mosquitto/data/
allow_anonymous false
log_dest file /mosquitto/log/mosquitto.log
listener 1883
password_file /mosquitto/passwd
```

```bash
docker restart mosquitto 
```

程序即可正常连接，这个版本如果没有配置密码是不能远程访问的额
## 报错
1.org.eclipse.paho.client.mqttv3.MqttSecurityException: 无权连接 此错误需要配置账号和密码

2.connect timeout 是端口没有开启端口有两个，一个是数据端口 一个websocket 端口。

# 编写测试程序

## 1.pom依赖

```xml
        <dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-web</artifactId>
		</dependency>
		<dependency>
			<groupId>org.eclipse.paho</groupId>
			<artifactId>org.eclipse.paho.client.mqttv3</artifactId>
			<version>1.2.5</version>
		</dependency>
		<dependency>
			<groupId>org.projectlombok</groupId>
			<artifactId>lombok</artifactId>
			<optional>true</optional>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-test</artifactId>
			<scope>test</scope>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-actuator</artifactId>
		</dependency>
```


## 2.配置文件

```bash
       # 服务器的端口号
        server.port=8080

        # MQTT 代理服务器的连接URI
        mqtt.broker=tcp://127.0.0.1:1883

        # MQTT 客户端ID，应保证在同一MQTT服务器上唯一
        mqtt.clientId=1

        # MQTT 默认的订阅话题
        mqtt.topic=testTopic

        # MQTT 代理服务器的用户名
        mqtt.username=root

        # MQTT 代理服务器的密码
        mqtt.password=123456

        # MQTT 链接是否启用清理会话，true 表示客户端与服务器断开连接后，会话信息将被清除，false 表示信息将会保留，以便客户端重新连接
        mqtt.cleanSession=true

        # MQTT 设置的心跳时间，即每隔多久时间（秒）客户端需要向服务端发送一个PINGREQ报文
        mqtt.keepAlive=60

        # MQTT 连接超时设置，指的是客户端连接到服务器时，等待CONNACK报文回应的最大时间间隔。
        mqtt.timeout=30

```

## 2.配置连接信息初始化client
```java

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
        return options;
    }

    @Bean
    public MqttClient mqttClient() throws MqttException {
        MqttClient mqttClient = new MqttClient(mqttBroker, MqttClient.generateClientId());
        mqttClient.connect(mqttConnectOptions());
        return mqttClient;
    }
}

```