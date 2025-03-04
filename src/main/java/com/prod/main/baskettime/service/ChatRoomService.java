package com.prod.main.baskettime.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.prod.main.baskettime.dto.ChatRoomUserDTO;
import com.prod.main.baskettime.entity.ChatRoom;
import com.prod.main.baskettime.repository.ChatRoomRepository;
import com.prod.main.baskettime.repository.ChatRoomUserRepository;

import jakarta.transaction.Transactional;

@Service
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomUserRepository chatRoomUserRepository;

    public ChatRoomService(ChatRoomRepository chatRoomRepository, ChatRoomUserRepository chatRoomUserRepository) {
        this.chatRoomRepository = chatRoomRepository;
        this.chatRoomUserRepository = chatRoomUserRepository;
    }

    // 채팅방 생성
    @Transactional
    public ChatRoom createChatRoom(String name, String description, int maxMembers) {
        ChatRoom chatRoom = new ChatRoom(name, description, maxMembers);
        return chatRoomRepository.save(chatRoom);
    }

    // 채팅방 리스트 조회
    public Page<ChatRoom> getAllChatRooms(int page, int size) {
        Pageable pageable = PageRequest.of(page, size); // 페이지 번호는 0부터 시작
        return chatRoomRepository.findAllByOrderByIdDesc(pageable);
        
    }

    // 채팅방 ID로 조회
    public Optional<ChatRoom> getChatRoomById(Long id) {
        return chatRoomRepository.findById(id);
    }

    public List<ChatRoomUserDTO> getChatRoomParticipants(Long roomId) {
        return chatRoomUserRepository.findParticipantsByRoomId(roomId);
    }
}
