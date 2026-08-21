package com.tractus.backend.controllers;

import com.tractus.backend.dtos.VoteRequest;
import com.tractus.backend.dtos.VoteResponse;
import com.tractus.backend.services.CommentVoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comment-votes")
public class CommentVoteController {

    @Autowired
    private CommentVoteService commentVoteService;

    @GetMapping("/comment/{commentId}")
    public List<VoteResponse> getVotesByComment(@PathVariable Long commentId) {
        return commentVoteService.getVotesByComment(commentId);
    }

    @PostMapping
    public VoteResponse castVote(@RequestBody VoteRequest request, Authentication authentication) {
        return commentVoteService.castVote(request, authentication);
    }
}
