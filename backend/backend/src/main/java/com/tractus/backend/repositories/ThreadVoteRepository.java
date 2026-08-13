package com.tractus.backend.repositories;

import com.tractus.backend.models.ThreadVote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ThreadVoteRepository extends JpaRepository<ThreadVote, Long> {
    List<ThreadVote> findByThreadId(Long threadId);
    java.util.Optional<ThreadVote> findByUserIdAndThreadId(Long userId, Long threadId);
}
