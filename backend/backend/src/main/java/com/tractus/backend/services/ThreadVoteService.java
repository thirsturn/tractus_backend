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
import com.tractus.backend.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
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

    public VoteResponse castVote(VoteRequest request, Authentication authentication) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Long authenticatedUserId = ((CustomUserDetails) authentication.getPrincipal()).getUser().getId();
        if (!authenticatedUserId.equals(user.getId())) {
            throw new RuntimeException("Cannot cast a vote as another user");
        }

        Thread thread = threadRepository.findById(request.getTargetId())
                .orElseThrow(() -> new RuntimeException("Thread not found"));

        java.util.Optional<ThreadVote> existingVoteOpt = threadVoteRepository.findByUserIdAndThreadId(user.getId(), thread.getId());

        ThreadVote voteToSave;
        if (existingVoteOpt.isPresent()) {
            ThreadVote existingVote = existingVoteOpt.get();
            if (existingVote.getVoteType() == request.getVoteType()) {
                // Toggle off: user clicked the same vote again, so remove it
                threadVoteRepository.delete(existingVote);
                return threadVoteMapper.toResponse(existingVote); // Returning the deleted vote structure to frontend so they know
            } else {
                // Change vote: e.g. UP to DOWN
                existingVote.setVoteType(request.getVoteType());
                voteToSave = existingVote;
            }
        } else {
            // New vote
            voteToSave = threadVoteMapper.toEntity(request);
            voteToSave.setUser(user);
            voteToSave.setThread(thread);
        }

        ThreadVote savedVote = threadVoteRepository.save(voteToSave);
        return threadVoteMapper.toResponse(savedVote);
    }
}
