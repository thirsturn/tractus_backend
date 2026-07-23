package com.tractus.backend.dtos;

import lombok.Data;

@Data
public class UserResponse {
    private Long id;
    private String username;
    private String email;
    private String bio;
    private String location;
    private String website;
    private String profileImageUrl;
    private long followerCount;
    private long followingCount;
}
