package com.tractus.backend.repositories;

import com.tractus.backend.models.Follow;
import com.tractus.backend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {

    // Count how many people follow this user
    long countByFollowing(User following);

    // Count how many people this user follows
    long countByFollower(User follower);

    // Check if a follow relationship already exists
    Optional<Follow> findByFollowerAndFollowing(User follower, User following);

    // Check existence for quick boolean checks
    boolean existsByFollowerAndFollowing(User follower, User following);
}
