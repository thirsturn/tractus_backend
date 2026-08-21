package com.tractus.backend.services;

import com.tractus.backend.models.Follow;
import com.tractus.backend.models.User;
import com.tractus.backend.repositories.FollowRepository;
import com.tractus.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FollowService {

    @Autowired
    private FollowRepository followRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationService notificationService;

    public long getFollowerCount(User user) {
        return followRepository.countByFollowing(user);
    }

    public long getFollowingCount(User user) {
        return followRepository.countByFollower(user);
    }

    public boolean isFollowing(User follower, User following) {
        return followRepository.existsByFollowerAndFollowing(follower, following);
    }

    public void follow(String followerUsername, String followingUsername) {
        if (followerUsername.equals(followingUsername)) {
            throw new RuntimeException("Users cannot follow themselves.");
        }

        User follower = userRepository.findByUsername(followerUsername)
                .orElseThrow(() -> new RuntimeException("Follower user not found."));
        User following = userRepository.findByUsername(followingUsername)
                .orElseThrow(() -> new RuntimeException("User to follow not found."));

        if (followRepository.existsByFollowerAndFollowing(follower, following)) {
            throw new RuntimeException("Already following this user.");
        }

        Follow follow = new Follow();
        follow.setFollower(follower);
        follow.setFollowing(following);
        followRepository.save(follow);

        notificationService.createNotification(
            following,
            follower,
            "FOLLOW",
            follower.getUsername() + " started following you",
            null
        );
    }

    public void unfollow(String followerUsername, String followingUsername) {
        User follower = userRepository.findByUsername(followerUsername)
                .orElseThrow(() -> new RuntimeException("Follower user not found."));
        User following = userRepository.findByUsername(followingUsername)
                .orElseThrow(() -> new RuntimeException("User to unfollow not found."));

        Follow follow = followRepository.findByFollowerAndFollowing(follower, following)
                .orElseThrow(() -> new RuntimeException("Not following this user."));

        followRepository.delete(follow);
    }
}
