package com.prod.main.baskettime.controller;

import java.util.List;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.prod.main.baskettime.entity.ChatMessage;
import com.prod.main.baskettime.repository.ChatMessageRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ChatMessageController {
    private final ChatMessageRepository chatMessageRepository;
    private final SimpMessagingTemplate messagingTemplate;

    // 채팅 메시지 전송 및 브로드캐스트
    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/chatroom/{roomId}")
    public ChatMessage sendMessage(ChatMessage chatMessage) {
        chatMessageRepository.save(chatMessage);
        return chatMessage;
    }

    // 채팅방 메시지 가져오기
    @GetMapping("/api/chatrooms/{roomId}/messages")
    public List<ChatMessage> getChatMessages(@PathVariable Long roomId) {
        return chatMessageRepository.findByRoomIdOrderByTimestampAsc(roomId);
    }
}
