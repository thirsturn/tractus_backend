package com.tractus.backend.services;

import com.tractus.backend.models.CommentVote;
import com.tractus.backend.repositories.CommentVoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentVoteService {

    @Autowired
    private CommentVoteRepository commentVoteRepository;

    public List<CommentVote> getVotesByComment(Long commentId) {
        return commentVoteRepository.findByCommentId(commentId);
    }

    public CommentVote castVote(CommentVote vote) {
        return commentVoteRepository.save(vote);
    }
}
