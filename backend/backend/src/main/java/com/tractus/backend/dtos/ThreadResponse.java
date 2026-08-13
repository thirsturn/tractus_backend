package com.tractus.backend.dtos;

import lombok.Data;

@Data
public class ThreadResponse {
    private Long id;
    private String title;
    private String content;
    private String imageUrl;
    private UserResponse author;
    private Long spaceId;
    private Integer commentCount = 0;
}
