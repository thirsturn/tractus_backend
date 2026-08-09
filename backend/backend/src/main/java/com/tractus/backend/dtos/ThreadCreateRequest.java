package com.tractus.backend.dtos;

import lombok.Data;

@Data
public class ThreadCreateRequest {
    private String title;
    private String content;
    private String imageUrl;
    private Long userId;
    private Long spaceId;
}
