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
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
@EntityListeners(AuditingEntityListener.class)  // 엔티티 리스너 추가
public class PushToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Users user; // 사용자 ID (ManyToOne 관계)

    @Column(name = "token", nullable = false)
    private String token; // FCM 푸시 토큰

    @Column(name = "device_type", nullable = false)
    private String deviceType; // "android" 또는 "ios"

    // 등록일자
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    // 수정일자
    @LastModifiedDate
    private LocalDateTime updatedAt;

    public PushToken() {}

    public PushToken(Users user, String token, String deviceType) {
        this.user = user;
        this.token = token;
        this.deviceType = deviceType;
    }

    // ✅ Getter & Setter
    public Long getId() { return id; }
    public Users getUser() { return user; }
    public String getToken() { return token; }
    public String getDeviceType() { return deviceType; }

    public void setUser(Users user) { this.user = user; }
    public void setToken(String token) { this.token = token; }
    public void setDeviceType(String deviceType) { this.deviceType = deviceType; }
    
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
