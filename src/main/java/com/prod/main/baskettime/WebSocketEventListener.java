// src/main/java/com/prod/main/baskettime/config/WebSocketEventListener.java

package com.prod.main.baskettime;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
public class WebSocketEventListener {

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectEvent event) {
        System.out.println("WebSocket 연결됨 : " + event);
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        System.out.println("WebSocket 연결 종료 : " + headerAccessor.getSessionId());
    }
}
