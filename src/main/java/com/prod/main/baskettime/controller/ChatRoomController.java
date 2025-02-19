package com.prod.main.baskettime.controller;

import com.prod.main.baskettime.entity.ChatRoom;
import com.prod.main.baskettime.service.ChatRoomService;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/chatrooms")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    public ChatRoomController(ChatRoomService chatRoomService) {
        this.chatRoomService = chatRoomService;
    }

    // 🔹 채팅방 생성 API
    @PostMapping
    public ResponseEntity<ChatRoom> createChatRoom(@RequestBody ChatRoom chatRoom) {
        ChatRoom createdRoom = chatRoomService.createChatRoom(chatRoom.getName(), chatRoom.getDescription(), chatRoom.getMaxMembers());
        return ResponseEntity.ok(createdRoom);
    }

    // 🔹 모든 채팅방 조회 API
    @GetMapping
    public Page<ChatRoom> getAllChatRooms(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size) {
        return chatRoomService.getAllChatRooms(page, size);
    }

    // 🔹 특정 채팅방 조회 API
    @GetMapping("/{id}")
    public ResponseEntity<ChatRoom> getChatRoomById(@PathVariable Long id) {
        Optional<ChatRoom> chatRoom = chatRoomService.getChatRoomById(id);
        return chatRoom.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
