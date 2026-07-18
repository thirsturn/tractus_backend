package com.tractus.backend.controllers;

import com.tractus.backend.models.CommentVote;
import com.tractus.backend.services.CommentVoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comment-votes")
public class CommentVoteController {

    @Autowired
    private CommentVoteService commentVoteService;

    @GetMapping("/comment/{commentId}")
    public List<CommentVote> getVotesByComment(@PathVariable Long commentId) {
        return commentVoteService.getVotesByComment(commentId);
    }

    @PostMapping
    public CommentVote castVote(@RequestBody CommentVote vote) {
        return commentVoteService.castVote(vote);
    }
}
