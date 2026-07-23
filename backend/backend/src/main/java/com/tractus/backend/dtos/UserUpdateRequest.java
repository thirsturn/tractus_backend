package com.tractus.backend.dtos;

import lombok.Data;

@Data
public class UserUpdateRequest {
    private String bio;
    private String location;
    private String website;
    private String profileImageUrl;
}
