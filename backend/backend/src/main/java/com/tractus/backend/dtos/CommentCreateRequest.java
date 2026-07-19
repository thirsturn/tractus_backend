package com.tractus.backend.dtos;

import lombok.Data;

@Data
public class CommentCreateRequest {
    private String content;
    private Long userId;
    private Long threadId;
    private Long parentCommentId; // optional
}
