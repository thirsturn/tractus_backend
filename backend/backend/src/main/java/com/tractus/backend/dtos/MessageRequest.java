package com.tractus.backend.dtos;

public class MessageRequest {
    private String recipientUsername;
    private String content;

    public MessageRequest() {}

    public MessageRequest(String recipientUsername, String content) {
        this.recipientUsername = recipientUsername;
        this.content = content;
    }

    public String getRecipientUsername() {
        return recipientUsername;
    }

    public void setRecipientUsername(String recipientUsername) {
        this.recipientUsername = recipientUsername;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
