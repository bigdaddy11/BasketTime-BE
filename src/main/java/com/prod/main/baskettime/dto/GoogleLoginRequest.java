package com.prod.main.baskettime.dto;

public class GoogleLoginRequest {
    private String subId;
    private String name;
    private String email;
    private String picture;

    public String getSubId() {
        return subId;
    }
    public void setSubId(String subId) {
        this.subId = subId;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPicture() {
        return picture;
    }
    public void setPicture(String picture) {
        this.picture = picture;
    }
}
