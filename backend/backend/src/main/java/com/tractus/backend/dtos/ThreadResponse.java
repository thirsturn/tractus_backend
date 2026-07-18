package com.tractus.backend.dtos;

import lombok.Data;

@Data
public class ThreadResponse {
    private Long id;
    private String title;
    private UserResponse author;
    private Long spaceId;
}
