package com.prod.main.baskettime.controller;

import com.prod.main.baskettime.dto.ChatRoomUserDTO;
import com.prod.main.baskettime.entity.ChatMessage;
import com.prod.main.baskettime.entity.ChatRoom;
import com.prod.main.baskettime.entity.ChatRoomUser;
import com.prod.main.baskettime.entity.Users;
import com.prod.main.baskettime.repository.ChatMessageRepository;
import com.prod.main.baskettime.repository.ChatRoomRepository;
import com.prod.main.baskettime.repository.ChatRoomUserRepository;
import com.prod.main.baskettime.repository.UserRepository;
import com.prod.main.baskettime.service.ChatRoomService;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/chatrooms")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomUserRepository chatRoomUserRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;

    public ChatRoomController(ChatRoomService chatRoomService, UserRepository userRepository, ChatRoomRepository chatRoomRepository, ChatRoomUserRepository chatRoomUserRepository, ChatMessageRepository chatMessageRepository) {
        this.chatRoomService = chatRoomService;
        this.chatRoomRepository = chatRoomRepository;
        this.chatRoomUserRepository = chatRoomUserRepository;
        this.chatMessageRepository =  chatMessageRepository;
        this.userRepository = userRepository;
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

    // ✅ 채팅방에 사용자 추가
    @PostMapping("/{roomId}/join/{userId}")
public ResponseEntity<String> joinChatRoom(@PathVariable Long roomId, @PathVariable Long userId) {
    ChatRoom chatRoom = chatRoomRepository.findById(roomId)
            .orElseThrow(() -> new RuntimeException("채팅방을 찾을 수 없음"));
    Users user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없음"));

    // 중복 가입 방지
    if (chatRoomUserRepository.findByChatRoomId(roomId).stream().anyMatch(u -> u.getUsers().getId().equals(userId))) {
        return ResponseEntity.ok("채팅방에 입장하였습니다.");
    }

    // 최대 인원 초과 방지
    if (chatRoomUserRepository.findByChatRoomId(roomId).size() >= chatRoom.getMaxMembers()) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("최대 인원을 초과하여 참여할 수 없습니다.");
    }

    // 채팅방 참여 등록
    ChatRoomUser chatRoomUser = new ChatRoomUser(chatRoom, user);
    chatRoomUserRepository.save(chatRoomUser);

    // ✅ 입장 메시지 추가
    String systemMessage = user.getNickName() + "님이 채팅방에 입장했습니다.";
    ChatMessage chatMessage = new ChatMessage(roomId, null, systemMessage, true);
    chatMessageRepository.save(chatMessage);

    return ResponseEntity.ok("채팅방에 입장하였습니다.");
}

    // 채팅방 참여자 목록 조회
    @GetMapping("/{roomId}/participants")
    public ResponseEntity<List<ChatRoomUserDTO>> getChatRoomParticipants(@PathVariable Long roomId) {
        List<ChatRoomUserDTO> participants = chatRoomService.getChatRoomParticipants(roomId);
        return ResponseEntity.ok(participants);
    }
    // public List<ChatRoomUser> getChatRoomParticipants(@PathVariable Long roomId) {
    //     return chatRoomUserRepository.findByChatRoomId(roomId);
    // }

    // ✅ 채팅방 나가기
    @DeleteMapping("/{roomId}/leave/{userId}")
    public ResponseEntity<String> leaveChatRoom(@PathVariable Long roomId, @PathVariable Long userId) {
        Optional<ChatRoomUser> chatRoomUser = chatRoomUserRepository.findByChatRoomId(roomId)
            .stream()
            .filter(u -> u.getUsers().getId().equals(userId))
            .findFirst(); // 해당 사용자의 참여 기록을 찾음
    
        if (chatRoomUser.isPresent()) {
            chatRoomUserRepository.delete(chatRoomUser.get()); // 특정 사용자만 삭제
            Users user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없음"));

            // 시스템 메시지 추가 (채팅방 퇴장)
            String systemMessage = user.getNickName() + "님이 채팅방에서 나갔습니다.";
            ChatMessage chatMessage = new ChatMessage(roomId, null, systemMessage, true);
            chatMessageRepository.save(chatMessage);
            return ResponseEntity.ok("채팅방에서 나갔습니다.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("채팅방에서 해당 사용자를 찾을 수 없습니다.");
        }
    }
}
