package com.prod.main.baskettime.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.Formula;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
@EntityListeners(AuditingEntityListener.class)  // 엔티티 리스너 추가
public class ChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 채팅방 ID

    @Column(nullable = false)
    private String name; // 채팅방 이름

    @Column(nullable = false)
    private String description; // 채팅방 소개

    @Column(nullable = false)
    private int maxMembers; // 최대 참여 가능 인원

    // 등록일자
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    // 수정일자
    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Formula("(SELECT COUNT(u.id) FROM chat_room_user u WHERE u.chat_room_id = id)")
    private int userCount;

    // 참여 중인 사용자 리스트
    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatRoomUser> participants = new ArrayList<>();

    public List<ChatRoomUser> getParticipants() {
        return participants;
    }

    public void setParticipants(List<ChatRoomUser> participants) {
        this.participants = participants;
    }

    // ✅ 추가: 참여한 유저 수 반환
    public int getUserCount() {
        return participants != null ? participants.size() : 0;
    }

    public ChatRoom() {}

    public ChatRoom(String name, String description, int maxMembers) {
        this.name = name;
        this.description = description;
        this.maxMembers = maxMembers;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getMaxMembers() {
        return maxMembers;
    }

    public void setMaxMembers(int maxMembers) {
        this.maxMembers = maxMembers;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
