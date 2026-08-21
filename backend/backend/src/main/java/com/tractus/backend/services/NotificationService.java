package com.tractus.backend.services;

import com.tractus.backend.dtos.NotificationResponse;
import com.tractus.backend.mappers.UserMapper;
import com.tractus.backend.models.Notification;
import com.tractus.backend.models.User;
import com.tractus.backend.repositories.NotificationRepository;
import com.tractus.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    public Notification createNotification(User recipient, User actor, String type, String message, Long targetThreadId) {
        if (recipient.getId().equals(actor.getId())) {
            // Don't notify self for own actions
            return null;
        }
        Notification notification = new Notification(recipient, actor, type, message, targetThreadId);
        return notificationRepository.save(notification);
    }

    public List<NotificationResponse> getUserNotifications(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));

        List<Notification> notifications = notificationRepository.findByRecipientOrderByCreatedAtDesc(user);
        return notifications.stream().map(this::toResponse).collect(Collectors.toList());
    }

    public void markAllAsRead(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));

        List<Notification> unread = notificationRepository.findByRecipientOrderByCreatedAtDesc(user)
                .stream().filter(n -> !n.isRead()).collect(Collectors.toList());

        for (Notification n : unread) {
            n.setRead(true);
        }
        notificationRepository.saveAll(unread);
    }

    public void markAsRead(Long notificationId, String username) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found: " + notificationId));

        if (notification.getRecipient().getUsername().equals(username)) {
            notification.setRead(true);
            notificationRepository.save(notification);
        }
    }

    private NotificationResponse toResponse(Notification notification) {
        NotificationResponse response = new NotificationResponse();
        response.setId(notification.getId());
        response.setActor(userMapper.toResponse(notification.getActor()));
        response.setType(notification.getType());
        response.setMessage(notification.getMessage());
        response.setTargetThreadId(notification.getTargetThreadId());
        response.setRead(notification.isRead());
        response.setCreatedAt(notification.getCreatedAt() != null ? notification.getCreatedAt().toString() : null);
        return response;
    }
}
