package com.tractus.backend.controllers;

import com.tractus.backend.dtos.CommentCreateRequest;
import com.tractus.backend.dtos.CommentResponse;
import com.tractus.backend.services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @GetMapping("/thread/{threadId}")
    public List<CommentResponse> getCommentsByThread(@PathVariable Long threadId) {
        return commentService.getCommentsByThread(threadId);
    }

    @PostMapping
    public CommentResponse createComment(@RequestBody CommentCreateRequest request, Authentication authentication) {
        return commentService.createComment(request, authentication);
    }
}
