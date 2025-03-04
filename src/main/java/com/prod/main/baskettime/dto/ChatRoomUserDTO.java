package com.prod.main.baskettime.dto;

import java.time.LocalDateTime;

public class ChatRoomUserDTO {
    private Long id;
    private String nickname;
    private String picture;
    private LocalDateTime joinedAt;

    public ChatRoomUserDTO(Long id, String nickname, String picture, LocalDateTime joinedAt) {
        this.id = id;
        this.nickname = nickname;
        this.picture = picture;
        this.joinedAt = joinedAt;
    }

    public Long getId() { return id; }
    public String getNickname() { return nickname; }
    public String getPicture() { return picture; }
    public LocalDateTime getJoinedAt() { return joinedAt; }
}