package com.prod.main.baskettime.entity;

import java.time.LocalDateTime;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;

@Entity
@EntityListeners(AuditingEntityListener.class)  // 엔티티 리스너 추가
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long roomId;         // 채팅방 ID
    private Long sender;       // 보낸 사람
    private String message;      // 메시지 내용
    private LocalDateTime timestamp; // 보낸 시간

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean isSystemMessage; // 시스템 메시지 여부 추가

    public boolean isSystemMessage() {
        return isSystemMessage;
    }

    public void setSystemMessage(boolean isSystemMessage) {
        this.isSystemMessage = isSystemMessage;
    }

    @PrePersist
    public void prePersist() {
        this.timestamp = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public Long getSender() {
        return sender;
    }

    public void setSender(Long sender) {
        this.sender = sender;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public ChatMessage(Long roomId, Long sender, String message, boolean isSystemMessage) {
        this.roomId = roomId;
        this.sender = sender;
        this.message = message;
        this.isSystemMessage = isSystemMessage;
        this.timestamp = LocalDateTime.now();
    }

    public ChatMessage() {}
}
