package com.tractus.backend.services;

import com.tractus.backend.models.ThreadVote;
import com.tractus.backend.repositories.ThreadVoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ThreadVoteService {

    @Autowired
    private ThreadVoteRepository threadVoteRepository;

    public List<ThreadVote> getVotesByThread(Long threadId) {
        return threadVoteRepository.findByThreadId(threadId);
    }

    public ThreadVote castVote(ThreadVote vote) {
        return threadVoteRepository.save(vote);
    }
}
