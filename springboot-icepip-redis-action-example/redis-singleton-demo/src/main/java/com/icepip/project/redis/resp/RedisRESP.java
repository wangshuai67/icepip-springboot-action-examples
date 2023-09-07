package com.icepip.project.redis.resp;

import java.io.*;
import java.net.*;
/**
 * Redis协议 RESP 学习和用java 原生简单实现
 * 本代码示例 对应博客 《【高阶篇】Redis协议(RESP )详解》感谢大家指正
 * @author 冰点
 * @version 1.0.0
 * @date 2023/9/7 17:42
 */
public class RedisRESP {
    public static void main(String[] args) throws IOException {
        // 创建一个 socket 连接
        Socket socket = new Socket("127.0.0.1", 6379);

        // 获取输出流，用于向服务器发送命令
        BufferedWriter out = new BufferedWriter(
                new OutputStreamWriter(
                        socket.getOutputStream(), "UTF-8"));

        // 获取输入流，用于接收服务器的响应
        BufferedReader in = new BufferedReader(
                new InputStreamReader(
                        socket.getInputStream(), "UTF-8"));

     // TODO 如果 Redis 服务器需要密码进行身份验证 如果不需要密码 则去掉这块代码验证
        String password = "123456";
        if (!password.isEmpty()) {
            // 发送 AUTH 命令进行身份验证
            out.write("AUTH " + password + "\r\n");
            out.flush();

            // 读取服务器响应
            String response = in.readLine();
            System.out.println("响应："+response);
            if (response.contains("OK")) {
                // 身份验证成功
                System.out.println("Authentication successful");
            } else {
                // 身份验证失败
                System.out.println("Authentication failed");
                // 进行错误处理或关闭连接等操作
                return;
            }
        }

        // 字符串数据类型
        // 使用 SET 命令设置一个键值对
        sendCommand(out, in, "*3\r\n$3\r\nSET\r\n$3\r\nkey\r\n$5\r\nvalue\r\n");
        // 使用 GET 命令获取键的值
        sendCommand(out, in, "*2\r\n$3\r\nGET\r\n$3\r\nkey\r\n");

        // 列表数据类型
        // 使用 RPUSH 命令将一个元素添加到列表的右侧
        sendCommand(out, in, "*3\r\n$5\r\nRPUSH\r\n$4\r\nlist\r\n$5\r\nvalue\r\n");
        // 使用 LPOP 命令移除并获取列表的第一个元素
        sendCommand(out, in, "*2\r\n$4\r\nLPOP\r\n$4\r\nlist\r\n");

        // 集合数据类型
        // 使用 SADD 命令添加一个元素到集合
        sendCommand(out, in, "*3\r\n$4\r\nSADD\r\n$3\r\nset\r\n$5\r\nvalue\r\n");
        // 使用 SPOP 命令随机移除并返回一个元素
        sendCommand(out, in, "*2\r\n$4\r\nSPOP\r\n$3\r\nset\r\n");

        // 有序集合数据类型
        // 使用 ZADD 命令添加一个元素到有序集合，为元素设置分数为0
        sendCommand(out, in, "*4\r\n$4\r\nZADD\r\n$4\r\nzset\r\n$1\r\n0\r\n$5\r\nvalue\r\n");
        // 使用 ZRANGE 命令返回有序集合中所有元素，按分数从小到大排序
        sendCommand(out, in, "*4\r\n$6\r\nZRANGE\r\n$4\r\nzset\r\n$1\r\n0\r\n$2\r\n-1\r\n");

        // 散列表数据类型
        // 使用 HSET 命令向散列表添加一个键值对
        sendCommand(out, in, "*4\r\n$4\r\nHSET\r\n$4\r\nhash\r\n$5\r\nfield\r\n$5\r\nvalue\r\n");
        // 使用 HGET 命令获取散列表中指定字段的值
        sendCommand(out, in, "*3\r\n$4\r\nHGET\r\n$4\r\nhash\r\n$5\r\nfield\r\n");

        // 关闭连接
        in.close();
        out.close();
        socket.close();
    }


    /**
     *  辅助方法，用于发送命令并打印响应
     * @param out
     * @param in
     * @param command
     * @throws IOException
     */
    private static void sendCommand(BufferedWriter out, BufferedReader in, String command) throws IOException {
        System.out.println("发送："+command);
        out.write(command);    // 向服务器发送命令
        out.flush();           // 清空缓冲区，确保命令立即发送
        System.out.println("接收："+in.readLine()); // 读取并打印服务器的响应
    }
}
