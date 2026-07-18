package com.tractus.backend.services;

import com.tractus.backend.dtos.CommentCreateRequest;
import com.tractus.backend.dtos.CommentResponse;
import com.tractus.backend.mappers.CommentMapper;
import com.tractus.backend.models.Comment;
import com.tractus.backend.models.Thread;
import com.tractus.backend.models.User;
import com.tractus.backend.repositories.CommentRepository;
import com.tractus.backend.repositories.ThreadRepository;
import com.tractus.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ThreadRepository threadRepository;
    @Autowired
    private CommentMapper commentMapper;

    public List<CommentResponse> getCommentsByThread(Long threadId) {
        return commentRepository.findByThreadId(threadId).stream()
                .map(commentMapper::toResponse)
                .collect(Collectors.toList());
    }

    public CommentResponse createComment(CommentCreateRequest request) {
        Comment comment = commentMapper.toEntity(request);
        
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Thread thread = threadRepository.findById(request.getThreadId())
                .orElseThrow(() -> new RuntimeException("Thread not found"));
                
        comment.setUser(user);
        comment.setThread(thread);
        
        if (request.getParentCommentId() != null) {
            Comment parent = commentRepository.findById(request.getParentCommentId())
                    .orElseThrow(() -> new RuntimeException("Parent comment not found"));
            comment.setParentComment(parent);
        }
        
        Comment savedComment = commentRepository.save(comment);
        return commentMapper.toResponse(savedComment);
    }
}
