package com.tractus.backend.repositories;

import com.tractus.backend.models.Thread;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ThreadRepository extends JpaRepository<Thread, Long> {
    List<Thread> findBySpaceId(Long spaceId); // Custom method to find threads by space
}
