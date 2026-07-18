package com.tractus.backend.dtos;

import lombok.Data;

@Data
public class SpaceCreateRequest {
    private String name;
    private String description;
}
