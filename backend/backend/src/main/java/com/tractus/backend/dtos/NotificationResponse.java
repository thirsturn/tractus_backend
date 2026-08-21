package com.tractus.backend.dtos;

public class NotificationResponse {
    private Long id;
    private UserResponse actor;
    private String type;
    private String message;
    private Long targetThreadId;
    private boolean isRead;
    private String createdAt;

    public NotificationResponse() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserResponse getActor() {
        return actor;
    }

    public void setActor(UserResponse actor) {
        this.actor = actor;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getTargetThreadId() {
        return targetThreadId;
    }

    public void setTargetThreadId(Long targetThreadId) {
        this.targetThreadId = targetThreadId;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
