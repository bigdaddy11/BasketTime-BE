package com.prod.main.baskettime.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
@EntityListeners(AuditingEntityListener.class)  // 엔티티 리스너 추가
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String subId;
    private String picture;
    private String email;
    private String name;
    private String type;    // G : google, N : Naver, K: Kakao
    private String nickName;
    
    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT false")
    private boolean editIs = false;

    // 등록일자
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    // 수정일자
    @LastModifiedDate
    private LocalDateTime updatedAt;

    
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getNickName() {
        return nickName;
    }
    public void setNickName(String nickName) {
        this.nickName = nickName;
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

    public String getPicture() {
        return picture;
    }
    public void setPicture(String picture) {
        this.picture = picture;
    }
    public String getSubId() {
        return subId;
    }
    public void setSubId(String subId) {
        this.subId = subId;
    }
    public boolean isEditIs() {
        return editIs;
    }
    public void setEditIs(boolean editIs) {
        this.editIs = editIs;
    }
}
