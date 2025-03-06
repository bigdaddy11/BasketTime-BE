package com.prod.main.baskettime.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.prod.main.baskettime.dto.ChatMessageDTO;
import com.prod.main.baskettime.entity.ChatMessage;
import com.prod.main.baskettime.entity.Users;
import com.prod.main.baskettime.repository.ChatMessageRepository;
import com.prod.main.baskettime.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ChatMessageController {
    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;
    private final SimpMessagingTemplate messagingTemplate;

    // // 채팅 메시지 전송 및 브로드캐스트
    // @MessageMapping("/chat.sendMessage")
    // @SendTo("/topic/chatroom/{roomId}")
    // public ChatMessage sendMessage(ChatMessage chatMessage) {
    //     chatMessageRepository.save(chatMessage);
    //     return chatMessage;
    // }

    // // 채팅방 메시지 가져오기
    // @GetMapping("/api/chatrooms/{roomId}/messages")
    // public List<ChatMessage> getChatMessages(@PathVariable Long roomId) {
    //     return chatMessageRepository.findByRoomIdOrderByTimestampAsc(roomId);
    // }

     // ✅ 채팅 메시지 전송 및 특정 채팅방으로 메시지 브로드캐스트
    @MessageMapping("/chat/{roomId}")
    public void sendMessage(@DestinationVariable Long roomId, ChatMessage message) {
        message.setRoomId(roomId);  // 메시지에 roomId 설정

        Users senderUser = userRepository.findById(message.getSender())
        .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없음"));

        String NickName = senderUser.getNickName(); // 닉네임 설정

        // 🔹 DTO 변환 후 WebSocket으로 전송
        ChatMessageDTO messageDTO = new ChatMessageDTO(
            message.getId(),
            message.getRoomId(),
            message.getSender(),
            NickName,
            message.getMessage(),
            LocalDateTime.now(),
            false
        );

        chatMessageRepository.save(message);  // 메시지 저장
        messagingTemplate.convertAndSend("/topic/chat/" + roomId, messageDTO); // 해당 채팅방으로 전송
    }

    // ✅ 특정 채팅방의 메시지 가져오기
    @GetMapping("/api/chatrooms/{roomId}/messages")
    public List<ChatMessageDTO> getChatMessages(@PathVariable Long roomId) {
        return chatMessageRepository.findChatMessagesWithSenderNickname(roomId);
    }

    // ✅ 사용자 입장 메시지 처리
    @MessageMapping("/chat/{roomId}/addUser")
    public void addUser(@DestinationVariable String roomId, ChatMessage message, SimpMessageHeaderAccessor headerAccessor) {
        headerAccessor.getSessionAttributes().put("username", message.getSender());
        message.setMessage(message.getSender() + "님이 입장하셨습니다.");
        messagingTemplate.convertAndSend("/topic/chat/" + roomId, message);
    }
}
