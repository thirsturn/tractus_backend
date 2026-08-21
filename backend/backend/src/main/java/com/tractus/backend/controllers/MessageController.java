package com.tractus.backend.controllers;

import com.tractus.backend.dtos.MessageRequest;
import com.tractus.backend.dtos.MessageResponse;
import com.tractus.backend.services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @PostMapping
    public ResponseEntity<MessageResponse> sendMessage(@RequestBody MessageRequest request, Authentication authentication) {
        String currentUsername = authentication.getName();
        MessageResponse response = messageService.sendMessage(currentUsername, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/conversations")
    public ResponseEntity<List<MessageResponse>> getUserConversations(Authentication authentication) {
        String currentUsername = authentication.getName();
        List<MessageResponse> conversations = messageService.getUserConversations(currentUsername);
        return ResponseEntity.ok(conversations);
    }

    @GetMapping("/conversation/{username}")
    public ResponseEntity<List<MessageResponse>> getConversation(@PathVariable String username, Authentication authentication) {
        String currentUsername = authentication.getName();
        List<MessageResponse> conversation = messageService.getConversation(currentUsername, username);
        return ResponseEntity.ok(conversation);
    }
}
