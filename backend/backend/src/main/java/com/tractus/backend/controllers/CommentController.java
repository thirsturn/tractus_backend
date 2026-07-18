package com.tractus.backend.controllers;

import com.tractus.backend.models.Comment;
import com.tractus.backend.services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @GetMapping("/thread/{threadId}")
    public List<Comment> getCommentsByThread(@PathVariable Long threadId) {
        return commentService.getCommentsByThread(threadId);
    }

    @PostMapping
    public Comment createComment(@RequestBody Comment comment) {
        return commentService.createComment(comment);
    }
}
