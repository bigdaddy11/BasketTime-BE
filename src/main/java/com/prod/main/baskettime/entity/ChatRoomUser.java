package com.prod.main.baskettime.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
@EntityListeners(AuditingEntityListener.class)  // 엔티티 리스너 추가
@JsonIgnoreProperties({"chatRoom", "users"}) // 무한 순환 방지
public class ChatRoomUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) // 채팅방과 N:1 관계
    @JoinColumn(name = "chat_room_id", nullable = false)
    private ChatRoom chatRoom;

    @ManyToOne // 사용자와 N:1 관계
    @JoinColumn(name = "user_id", nullable = false)
    private Users users;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime joinedAt; // 참여 시간

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ChatRoom getChatRoom() {
        return chatRoom;
    }

    public void setChatRoom(ChatRoom chatRoom) {
        this.chatRoom = chatRoom;
    }

    public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {
        this.users = users;
    }

    public LocalDateTime getJoinedAt() {
        return joinedAt;
    }

    public void setJoinedAt(LocalDateTime joinedAt) {
        this.joinedAt = joinedAt;
    }

    // 기본 생성자 추가 (JPA가 객체를 생성할 때 필요)
    public ChatRoomUser() {}

    public ChatRoomUser(ChatRoom chatRoom, Users users) {
        this.chatRoom = chatRoom;
        this.users = users;
        this.joinedAt = LocalDateTime.now();
    }
}
