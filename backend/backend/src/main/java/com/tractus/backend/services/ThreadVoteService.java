package com.tractus.backend.services;

import com.tractus.backend.dtos.VoteRequest;
import com.tractus.backend.dtos.VoteResponse;
import com.tractus.backend.mappers.ThreadVoteMapper;
import com.tractus.backend.models.Thread;
import com.tractus.backend.models.ThreadVote;
import com.tractus.backend.models.User;
import com.tractus.backend.repositories.ThreadRepository;
import com.tractus.backend.repositories.ThreadVoteRepository;
import com.tractus.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ThreadVoteService {

    @Autowired
    private ThreadVoteRepository threadVoteRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ThreadRepository threadRepository;
    @Autowired
    private ThreadVoteMapper threadVoteMapper;

    public List<VoteResponse> getVotesByThread(Long threadId) {
        return threadVoteRepository.findByThreadId(threadId).stream()
                .map(threadVoteMapper::toResponse)
                .collect(Collectors.toList());
    }

    public VoteResponse castVote(VoteRequest request) {
        ThreadVote vote = threadVoteMapper.toEntity(request);
        
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Thread thread = threadRepository.findById(request.getTargetId())
                .orElseThrow(() -> new RuntimeException("Thread not found"));
                
        vote.setUser(user);
        vote.setThread(thread);
        
        ThreadVote savedVote = threadVoteRepository.save(vote);
        return threadVoteMapper.toResponse(savedVote);
    }
}
