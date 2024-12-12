package com.prod.main.baskettime.entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;

import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)  // 엔티티 리스너 추가
public class PlayerComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type;    // NBA or KBL
    private Long playerId;  // 댓글이 달린 선수의 ID
    private Long userId;    // 댓글 달은 유저
    private String commentText; // 댓글

    @Transient // DB에 저장되지 않음
    private String nickName;

    @Transient // DB에 저장되지 않음
    private String timeAgo;

    // 등록일자
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    // 수정일자
    @LastModifiedDate
    private LocalDateTime updatedAt;

    // 기본 생성자 및 Getter, Setter
    public PlayerComment() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    // 생성자 및 getter, setter 추가
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
    
    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getTimeAgo() {
        return timeAgo;
    }

    public void setTimeAgo(String timeAgo) {
        this.timeAgo = timeAgo;
    }
};