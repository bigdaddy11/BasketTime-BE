package com.prod.main.baskettime.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.prod.main.baskettime.dto.ChatMessageDTO;
import com.prod.main.baskettime.entity.ChatMessage;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findByRoomIdOrderByTimestampAsc(Long roomId);

     // ✅ 특정 채팅방의 메시지를 보낸 사람의 닉네임과 함께 조회
    @Query("SELECT new com.prod.main.baskettime.dto.ChatMessageDTO(cm.id, cm.roomId, cm.sender, u.nickName, cm.message, cm.timestamp, cm.isSystemMessage) " +
           "FROM ChatMessage cm " +
           "LEFT JOIN Users u ON cm.sender = u.id " +
           "WHERE cm.roomId = :roomId " +
           "ORDER BY cm.timestamp ASC")
    List<ChatMessageDTO> findChatMessagesWithSenderNickname(@Param("roomId") Long roomId);
}
