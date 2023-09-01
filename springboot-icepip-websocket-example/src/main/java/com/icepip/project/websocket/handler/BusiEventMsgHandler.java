package com.icepip.project.websocket.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.thymeleaf.util.DateUtils;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
/**
 * 第一种方式 使用JSR 356 Java WebSocket API
 * 暴露websocket 端点为 /busiEventHandler
 * 客户端连接方式使用    ws = new WebSocket("ws://localhost:8080/busiEventHandler?userId=" + userId);
 * @author 冰点
 * @version 1.0.0
 * @date 2023/9/1 13:54
 */

@ServerEndpoint("/busiEventHandler")
@Component
@Slf4j
public class BusiEventMsgHandler {
    private static final ConcurrentHashMap<String, Session> sessions = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session) {
        // 假设我们在查询参数中有一个用户ID
        String userId = session.getRequestParameterMap().get("userId").get(0);
        sessions.put(userId, session);
    }

    public void sendMessageToClients(String userId, String message) {
        Session session = sessions.get(userId);
        if (session != null) {
            try {
                session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @OnClose
    public void onClose() {
        System.out.println("onClose");
        // 处理 WebSocket 连接关闭
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        List<String> userId = session.getRequestParameterMap().get("userId");
        log.info("收到userId:{}的消息{}",userId,message);
        Date date = new Date();
        try {
            session.getBasicRemote().sendText("时间："+ date.toString() +"收到userId:"+userId+"的消息:"+message);
        } catch (IOException e) {
             log.error("发送消息失败",e);
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        System.out.println("onError");
        error.printStackTrace();
        // 处理 WebSocket 错误
    }
}
