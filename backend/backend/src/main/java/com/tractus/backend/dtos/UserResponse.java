package com.tractus.backend.dtos;

import lombok.Data;

import java.time.LocalDate;

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
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private LocalDate dateOfBirth;
    private String gender;
}
