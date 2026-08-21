package com.tractus.backend.dtos;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UserCreateRequest {
    private String username;
    private String email;
    private String passwordHash;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private LocalDate dateOfBirth;
    private String gender;
}
