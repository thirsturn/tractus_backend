package com.tractus.backend.controllers;

import com.tractus.backend.dtos.UserCreateRequest;
import com.tractus.backend.dtos.UserUpdateRequest;
import com.tractus.backend.dtos.UserResponse;
import com.tractus.backend.services.FollowService;
import com.tractus.backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private FollowService followService;

    @GetMapping
    public List<UserResponse> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping
    public UserResponse createUser(@RequestBody UserCreateRequest request) {
        return userService.createUser(request);
    }

    @GetMapping("/{username}")
    public ResponseEntity<UserResponse> getUserByUsername(@PathVariable String username, Authentication authentication) {
        return userService.getUserByUsername(username, authentication)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable Long id, @RequestBody UserUpdateRequest request, Authentication authentication) {
        try {
            UserResponse updatedUser = userService.updateUser(id, request, authentication);
            return ResponseEntity.ok(updatedUser);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/avatar")
    public ResponseEntity<UserResponse> uploadAvatar(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        try {
            UserResponse updatedUser = userService.uploadAvatar(id, file);
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/{username}/follow")
    public ResponseEntity<String> followUser(@PathVariable String username, Authentication authentication) {
        try {
            String currentUsername = authentication.getName();
            followService.follow(currentUsername, username);
            return ResponseEntity.ok("Followed " + username);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{username}/follow")
    public ResponseEntity<String> unfollowUser(@PathVariable String username, Authentication authentication) {
        try {
            String currentUsername = authentication.getName();
            followService.unfollow(currentUsername, username);
            return ResponseEntity.ok("Unfollowed " + username);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
