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

import java.util.List;
import java.util.Optional;
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

        User updatedUser = userRepository.save(user);
        return enrichWithCounts(updatedUser);
    }
}
