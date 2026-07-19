package com.tractus.backend.dtos;

import lombok.Data;

@Data
public class CommentResponse {
    private Long id;
    private String content;
    private UserResponse author;
    private Long threadId;
    private Long parentCommentId; // optional
}
