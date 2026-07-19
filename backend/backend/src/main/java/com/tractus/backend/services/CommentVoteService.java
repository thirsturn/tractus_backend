package com.tractus.backend.services;

import com.tractus.backend.dtos.VoteRequest;
import com.tractus.backend.dtos.VoteResponse;
import com.tractus.backend.mappers.CommentVoteMapper;
import com.tractus.backend.models.Comment;
import com.tractus.backend.models.CommentVote;
import com.tractus.backend.models.User;
import com.tractus.backend.repositories.CommentRepository;
import com.tractus.backend.repositories.CommentVoteRepository;
import com.tractus.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentVoteService {

    @Autowired
    private CommentVoteRepository commentVoteRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private CommentVoteMapper commentVoteMapper;

    public List<VoteResponse> getVotesByComment(Long commentId) {
        return commentVoteRepository.findByCommentId(commentId).stream()
                .map(commentVoteMapper::toResponse)
                .collect(Collectors.toList());
    }

    public VoteResponse castVote(VoteRequest request) {
        CommentVote vote = commentVoteMapper.toEntity(request);
        
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Comment comment = commentRepository.findById(request.getTargetId())
                .orElseThrow(() -> new RuntimeException("Comment not found"));
                
        vote.setUser(user);
        vote.setComment(comment);
        
        CommentVote savedVote = commentVoteRepository.save(vote);
        return commentVoteMapper.toResponse(savedVote);
    }
}
