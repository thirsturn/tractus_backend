package com.tractus.backend.services;

import com.tractus.backend.dtos.UserCreateRequest;
import com.tractus.backend.dtos.UserUpdateRequest;
import com.tractus.backend.dtos.UserResponse;
import com.tractus.backend.mappers.UserMapper;
import com.tractus.backend.models.User;
import com.tractus.backend.repositories.FollowRepository;
import com.tractus.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private FollowRepository followRepository;

    // Enrich a UserResponse with follower/following counts
    private UserResponse enrichWithCounts(User user) {
        UserResponse response = userMapper.toResponse(user);
        response.setFollowerCount(followRepository.countByFollowing(user));
        response.setFollowingCount(followRepository.countByFollower(user));
        return response;
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::enrichWithCounts)
                .collect(Collectors.toList());
    }

    @Autowired
    private org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    public UserResponse createUser(UserCreateRequest request) {
        String pwd = request.getPasswordHash(); // The DTO probably uses this field for the raw password
        if (pwd == null || pwd.length() < 8 || !pwd.matches(".*[A-Z].*") || !pwd.matches(".*[a-z].*") || !pwd.matches(".*\\d.*") || !pwd.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*")) {
            throw new IllegalArgumentException("Password must be at least 8 characters long and contain uppercase, lowercase, number, and special character.");
        }
        User user = userMapper.toEntity(request);
        user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));
        User savedUser = userRepository.save(user);
        return enrichWithCounts(savedUser);
    }

    public Optional<UserResponse> getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(this::enrichWithCounts);
    }

    public UserResponse updateUser(Long id, UserUpdateRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        if (request.getBio() != null) {
            user.setBio(request.getBio());
        }
        if (request.getLocation() != null) {
            user.setLocation(request.getLocation());
        }
        if (request.getWebsite() != null) {
            user.setWebsite(request.getWebsite());
        }

        if (request.getProfileImageUrl() != null) {
            user.setProfileImageUrl(request.getProfileImageUrl());
        }
        if (request.getPassword() != null && !request.getPassword().trim().isEmpty()) {
            if (request.getCurrentPassword() == null || !passwordEncoder.matches(request.getCurrentPassword(), user.getPasswordHash())) {
                throw new IllegalArgumentException("Current password is incorrect");
            }
            user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        }

        User updatedUser = userRepository.save(user);
        return enrichWithCounts(updatedUser);
    }

    public UserResponse uploadAvatar(Long id, MultipartFile file) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        try {
            String uploadDir = "uploads/avatars/";
            Path uploadPath = Paths.get(uploadDir);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename != null && originalFilename.contains(".") ? originalFilename.substring(originalFilename.lastIndexOf(".")) : "";
            String newFilename = UUID.randomUUID().toString() + extension;

            Path filePath = uploadPath.resolve(newFilename);
            Files.copy(file.getInputStream(), filePath, java.nio.file.StandardCopyOption.REPLACE_EXISTING);

            String fileUrl = "http://localhost:8080/" + uploadDir + newFilename;
            user.setProfileImageUrl(fileUrl);
            User updatedUser = userRepository.save(user);
            return enrichWithCounts(updatedUser);
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file", e);
        }
    }
}
