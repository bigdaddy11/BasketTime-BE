package com.prod.main.baskettime.dto;

public class PaperPlanWithNickName {
    private Long id;
    private String content;
    private String nickName;
    private String timeAgo; // 새 필드 추가
    private Boolean isRead;

    public Boolean getIsRead() {
        return isRead;
    }

    public void setIsRead(Boolean isRead) {
        this.isRead = isRead;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public PaperPlanWithNickName(Long id, String content, String nickName, String timeAgo, Boolean isRead) {
        this.id = id;
        this.content = content;
        this.nickName = nickName;
        this.timeAgo = timeAgo;
        this.isRead = isRead;
    }

    public String getTimeAgo() {
        return timeAgo;
    }

    public void setTimeAgo(String timeAgo) {
        this.timeAgo = timeAgo;
    }
}
