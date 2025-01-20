package com.prod.main.baskettime.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.ColumnResult;
import jakarta.persistence.ConstructorResult;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SqlResultSetMapping;
import jakarta.persistence.Table;

@Entity
@Table(name = "paperPlan")
@SqlResultSetMapping(
    name = "PaperPlanWithNickNameMapping",
    classes = @ConstructorResult(
        targetClass = com.prod.main.baskettime.dto.PaperPlanWithNickName.class,
        columns = {
            @ColumnResult(name = "id", type = Long.class),
            @ColumnResult(name = "content", type = String.class),
            @ColumnResult(name = "nickName", type = String.class),
            @ColumnResult(name = "timeAgo", type = String.class),
            @ColumnResult(name = "isRead", type = Boolean.class)
        }
    )
)
@EntityListeners(AuditingEntityListener.class)  // 엔티티 리스너 추가
public class PaperPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long sUserId; // 보내는 유저 ID

    @Column(nullable = false)
    private Long rUserId; // 받는 유저 ID

    @Column(nullable = false, length = 1000)
    private String content; // 쪽지 내용

    @Column
    private Boolean isRead; // 받는 유저 ID

    // 등록일자
    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getsUserId() {
        return sUserId;
    }

    public void setsUserId(Long sUserId) {
        this.sUserId = sUserId;
    }

    public Long getrUserId() {
        return rUserId;
    }

    public void setrUserId(Long rUserId) {
        this.rUserId = rUserId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Boolean getIsRead() {
        return isRead;
    }

    public void setIsRead(Boolean isRead) {
        this.isRead = isRead;
    }
}
