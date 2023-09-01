package com.icepip.project.websocket.handler;

import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/alarmHandler")
@Component
public class AlarmMsgHandler {
    @OnOpen
    public void onOpen(Session session) {
        // 处理新的 WebSocket 连接
    }

    @OnClose
    public void onClose() {
        // 处理 WebSocket 连接关闭
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        // 处理 WebSocket 消息
    }

    @OnError
    public void onError(Session session, Throwable error) {
        // 处理 WebSocket 错误
    }
}
