package com.tractus.backend.services;

import com.tractus.backend.dtos.UserCreateRequest;
import com.tractus.backend.dtos.UserResponse;
import com.tractus.backend.mappers.UserMapper;
import com.tractus.backend.models.User;
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

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toResponse)
                .collect(Collectors.toList());
    }

    public UserResponse createUser(UserCreateRequest request) {
        User user = userMapper.toEntity(request);
        // Normally we'd hash password here using BCrypt before saving
        User savedUser = userRepository.save(user);
        return userMapper.toResponse(savedUser);
    }

    public Optional<UserResponse> getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(userMapper::toResponse);
    }
}
