package com.tractus.backend.repositories;

import com.tractus.backend.models.CommentVote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentVoteRepository extends JpaRepository<CommentVote, Long> {
    List<CommentVote> findByCommentId(Long commentId);
}
