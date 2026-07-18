package com.tractus.backend.dtos;

import lombok.Data;

@Data
public class ThreadCreateRequest {
    private String title;
    private Long userId;
    private Long spaceId;
}
