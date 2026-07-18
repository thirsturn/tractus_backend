package com.tractus.backend.controllers;

import com.tractus.backend.models.ThreadVote;
import com.tractus.backend.services.ThreadVoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/thread-votes")
public class ThreadVoteController {

    @Autowired
    private ThreadVoteService threadVoteService;

    @GetMapping("/thread/{threadId}")
    public List<ThreadVote> getVotesByThread(@PathVariable Long threadId) {
        return threadVoteService.getVotesByThread(threadId);
    }

    @PostMapping
    public ThreadVote castVote(@RequestBody ThreadVote vote) {
        return threadVoteService.castVote(vote);
    }
}
