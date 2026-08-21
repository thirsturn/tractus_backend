package com.tractus.backend.services;

import com.tractus.backend.dtos.MessageRequest;
import com.tractus.backend.dtos.MessageResponse;
import com.tractus.backend.dtos.UserResponse;
import com.tractus.backend.mappers.UserMapper;
import com.tractus.backend.models.Message;
import com.tractus.backend.models.User;
import com.tractus.backend.repositories.MessageRepository;
import com.tractus.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private NotificationService notificationService;

    public MessageResponse sendMessage(String senderUsername, MessageRequest request) {
        User sender = userRepository.findByUsername(senderUsername)
                .orElseThrow(() -> new RuntimeException("Sender not found: " + senderUsername));

        User recipient = userRepository.findByUsername(request.getRecipientUsername())
                .orElseThrow(() -> new RuntimeException("Recipient not found: " + request.getRecipientUsername()));

        Message message = new Message(sender, recipient, request.getContent());
        Message saved = messageRepository.save(message);

        notificationService.createNotification(
            recipient,
            sender,
            "MESSAGE",
            sender.getUsername() + " sent you a private message",
            null
        );

        return toResponse(saved);
    }

    public List<MessageResponse> getConversation(String currentUsername, String otherUsername) {
        User user1 = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new RuntimeException("User not found: " + currentUsername));
        User user2 = userRepository.findByUsername(otherUsername)
                .orElseThrow(() -> new RuntimeException("User not found: " + otherUsername));

        List<Message> conversation = messageRepository.findConversation(user1, user2);
        return conversation.stream().map(this::toResponse).collect(Collectors.toList());
    }

    public List<MessageResponse> getUserConversations(String currentUsername) {
        User user = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new RuntimeException("User not found: " + currentUsername));

        List<Message> allMessages = messageRepository.findAllUserMessages(user);
        Map<Long, Message> latestPerPartner = new LinkedHashMap<>();

        for (Message msg : allMessages) {
            User partner = msg.getSender().getId().equals(user.getId()) ? msg.getRecipient() : msg.getSender();
            if (!latestPerPartner.containsKey(partner.getId())) {
                latestPerPartner.put(partner.getId(), msg);
            }
        }

        return latestPerPartner.values().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private MessageResponse toResponse(Message message) {
        MessageResponse response = new MessageResponse();
        response.setId(message.getId());
        response.setSender(userMapper.toResponse(message.getSender()));
        response.setRecipient(userMapper.toResponse(message.getRecipient()));
        response.setContent(message.getContent());
        response.setCreatedAt(message.getCreatedAt() != null ? message.getCreatedAt().toString() : null);
        response.setRead(message.isRead());
        return response;
    }
}
