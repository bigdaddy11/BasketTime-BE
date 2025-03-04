package com.prod.main.baskettime.dto;

import java.time.LocalDateTime;

public class ChatMessageDTO {
    private Long id;
    private Long roomId;
    private Long sender;
    private String senderNickname; // 추가: 닉네임
    private String message;
    private Boolean isSystemMessage;

    private LocalDateTime timestamp;

    public ChatMessageDTO(Long id, Long roomId, Long sender, String senderNickname, String message, LocalDateTime timestamp, Boolean isSystemMessage) {
        this.id = id;
        this.roomId = roomId;
        this.sender = sender;
        this.senderNickname = senderNickname;
        this.message = message;
        this.timestamp = timestamp;
        this.isSystemMessage = isSystemMessage;
    }

    public Long getId() {
        return id;
    }

    public Long getRoomId() {
        return roomId;
    }

    public Long getSender() {
        return sender;
    }

    public String getSenderNickname() {
        return senderNickname;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public Boolean getIsSystemMessage() {
        return isSystemMessage;
    }
}
