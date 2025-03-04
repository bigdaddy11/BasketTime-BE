package com.prod.main.baskettime.repository;

import com.prod.main.baskettime.dto.ChatRoomUserDTO;
import com.prod.main.baskettime.entity.ChatRoomUser;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ChatRoomUserRepository extends JpaRepository<ChatRoomUser, Long> {
    List<ChatRoomUser> findByChatRoomId(Long chatRoomId);

    @Query("SELECT new com.prod.main.baskettime.dto.ChatRoomUserDTO( " +
           "u.id, u.users.nickName, u.users.picture, u.joinedAt) " +
           "FROM ChatRoomUser u " +
           "WHERE u.chatRoom.id = :roomId")
    List<ChatRoomUserDTO> findParticipantsByRoomId(@Param("roomId") Long roomId);
}
