package com.tractus.backend.repositories;

import com.tractus.backend.models.Thread;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ThreadRepository extends JpaRepository<Thread, Long> {
    @Query("SELECT t FROM Thread t JOIN FETCH t.user WHERE t.space.id = :spaceId")
    List<Thread> findBySpaceId(@Param("spaceId") Long spaceId); // Custom method to find threads by space
}
