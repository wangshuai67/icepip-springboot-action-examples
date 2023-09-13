package com.icepip.project.mqtt.test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 *  使用最原始的方式 进行MQTT连接和使用。
 *  仅作为学习验证使用。
 * @author 冰点
 * @version 1.0.0
 * @date 2023/9/9 13:19
 */

public class MqttClientExample {

    private static final String BROKER = "172.20.6.xx"; // MQTT代理服务器的地址
    private static final int PORT = 1883;
    private static final String USERNAME = "root"; // MQTT代理服务器的用户名
    private static final String PASSWORD = "123456"; // MQTT代理服务器的密码

    public static void main(String[] args) {
        try {
            // 创建Socket连接
            Socket socket = new Socket(BROKER, PORT);
            InputStream inputStream = socket.getInputStream();
            OutputStream outputStream = socket.getOutputStream();

            // 发送CONNECT消息
            byte[] connectMessage = buildConnectMessage(USERNAME, PASSWORD);
            outputStream.write(connectMessage);

            // 接收CONNACK消息
            byte[] connackHeader = new byte[2];
            inputStream.read(connackHeader);
            byte connackMessageType = connackHeader[0];
            byte connackRemainingLength = connackHeader[1];
            byte[] connackPayload = new byte[connackRemainingLength];
            inputStream.read(connackPayload);

            if (connackMessageType == 0x20 && connackPayload[1] == 0x00) {
                System.out.println("Connected to MQTT broker");
            } else {
                System.out.println("Failed to connect to MQTT broker");
            }

            // 发送PUBLISH消息
            byte[] publishMessage = {0x30, 0x0D, 0x00, 0x09, 0x73, 0x65, 0x6E, 0x73, 0x6F, 0x72, 0x73, 0x2F, 0x74, 0x65, 0x73, 0x74};
            outputStream.write(publishMessage);

            // 接收PUBACK消息
            byte[] pubackHeader = new byte[4];
            inputStream.read(pubackHeader);
            byte pubackMessageType = pubackHeader[0];
            byte pubackRemainingLength = pubackHeader[1];

            if (pubackMessageType == 0x40 && pubackRemainingLength == 0x02) {
                System.out.println("Message published successfully");
            } else {
                System.out.println("Failed to publish message");
            }

            // 关闭连接
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static byte[] buildConnectMessage(String username, String password) {
        String clientId = "my_client";

        // 构建MQTT 5.0 Connect消息的固定报头
        byte[] fixedHeader = {(byte) 0x10}; // Connect消息类型

        // 构建MQTT 5.0 Connect消息的可变报头
        byte[] variableHeader = {
                0x00, 0x04, 'M', 'Q', 'T', 'T', // Protocol Name
                0x05, // Protocol Level (MQTT 5.0)
                (byte) 0xC2, // Connect Flags (Username, Password, Clean Start)
                0x00, 0x3C, // Keep Alive
                0x00, // Properties Length
        };

        // 构建MQTT 5.0 Connect消息的负载
        byte[] clientIdBytes = clientId.getBytes(StandardCharsets.UTF_8);
        byte[] usernameBytes = username.getBytes(StandardCharsets.UTF_8);
        byte[] passwordBytes = password.getBytes(StandardCharsets.UTF_8);
        byte[] payload = new byte[6 + clientIdBytes.length + usernameBytes.length + passwordBytes.length];
        int index = 0;
        payload[index++] = 0x00;
        payload[index++] = (byte) clientIdBytes.length;
        System.arraycopy(clientIdBytes, 0, payload, index, clientIdBytes.length);
        index += clientIdBytes.length;
        payload[index++] = 0x00;
        payload[index++] = (byte) usernameBytes.length;
        System.arraycopy(usernameBytes, 0, payload, index, usernameBytes.length);
        index += usernameBytes.length;
        payload[index++] = 0x00;
        payload[index++] = (byte) passwordBytes.length;
        System.arraycopy(passwordBytes, 0, payload, index, passwordBytes.length);
        index += passwordBytes.length;

        // 构建完整的MQTT 5.0 Connect消息
        byte[] connectMessage = new byte[1 + variableHeader.length + payload.length];
        connectMessage[0] = (byte) (variableHeader.length + payload.length); // Remaining Length
        System.arraycopy(variableHeader, 0, connectMessage, 1, variableHeader.length);
        System.arraycopy(payload, 0, connectMessage, 1 + variableHeader.length, payload.length);

        return connectMessage;
    }

}