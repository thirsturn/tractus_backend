package com.tractus.backend.controllers;

import com.tractus.backend.dtos.NotificationResponse;
import com.tractus.backend.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping
    public ResponseEntity<List<NotificationResponse>> getUserNotifications(Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(401).build();
        }
        String currentUsername = authentication.getName();
        return ResponseEntity.ok(notificationService.getUserNotifications(currentUsername));
    }

    @PutMapping("/read-all")
    public ResponseEntity<Void> markAllAsRead(Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(401).build();
        }
        String currentUsername = authentication.getName();
        notificationService.markAllAsRead(currentUsername);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<Void> markAsRead(@PathVariable Long id, Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(401).build();
        }
        String currentUsername = authentication.getName();
        notificationService.markAsRead(id, currentUsername);
        return ResponseEntity.ok().build();
    }
}
