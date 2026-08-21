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
import com.tractus.backend.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
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

    public VoteResponse castVote(VoteRequest request, Authentication authentication) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Long authenticatedUserId = ((CustomUserDetails) authentication.getPrincipal()).getUser().getId();
        if (!authenticatedUserId.equals(user.getId())) {
            throw new RuntimeException("Cannot cast a vote as another user");
        }

        Comment comment = commentRepository.findById(request.getTargetId())
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        java.util.Optional<CommentVote> existingVoteOpt = commentVoteRepository.findByUserIdAndCommentId(user.getId(), comment.getId());

        CommentVote voteToSave;
        if (existingVoteOpt.isPresent()) {
            CommentVote existingVote = existingVoteOpt.get();
            if (existingVote.getVoteType() == request.getVoteType()) {
                // Toggle off
                commentVoteRepository.delete(existingVote);
                return commentVoteMapper.toResponse(existingVote);
            } else {
                // Change vote
                existingVote.setVoteType(request.getVoteType());
                voteToSave = existingVote;
            }
        } else {
            voteToSave = commentVoteMapper.toEntity(request);
            voteToSave.setUser(user);
            voteToSave.setComment(comment);
        }

        CommentVote savedVote = commentVoteRepository.save(voteToSave);
        return commentVoteMapper.toResponse(savedVote);
    }
}
