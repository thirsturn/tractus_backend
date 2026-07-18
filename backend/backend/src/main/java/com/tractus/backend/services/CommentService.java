package com.tractus.backend.services;

import com.tractus.backend.models.Comment;
import com.tractus.backend.repositories.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    public List<Comment> getCommentsByThread(Long threadId) {
        return commentRepository.findByThreadId(threadId);
    }

    public Comment createComment(Comment comment) {
        return commentRepository.save(comment);
    }
}
