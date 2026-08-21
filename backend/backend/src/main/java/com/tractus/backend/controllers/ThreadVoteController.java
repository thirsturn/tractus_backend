package com.tractus.backend.controllers;

import com.tractus.backend.dtos.VoteRequest;
import com.tractus.backend.dtos.VoteResponse;
import com.tractus.backend.services.ThreadVoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/thread-votes")
public class ThreadVoteController {

    @Autowired
    private ThreadVoteService threadVoteService;

    @GetMapping("/thread/{threadId}")
    public List<VoteResponse> getVotesByThread(@PathVariable Long threadId) {
        return threadVoteService.getVotesByThread(threadId);
    }

    @PostMapping
    public VoteResponse castVote(@RequestBody VoteRequest request, Authentication authentication) {
        return threadVoteService.castVote(request, authentication);
    }
}
