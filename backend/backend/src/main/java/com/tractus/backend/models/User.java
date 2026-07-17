package com.tractus.backend.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.util.List;

@Entity // Marks this as a database table
@Table(name = "users") // Names the table "users"
@Data // Lombok: Getters and setters
@AllArgsConstructor // Lombok: all args constructor
@NoArgsConstructor // Lombok: no args constructor
public class User {
    
    @Id // Primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increment ID
    private Long id; 

    @Column(unique=true, nullable=false) // Ensure no two users have same name
    private String username; 

    @Column(unique=true, nullable=false) // Ensure unique email
    private String email;

    @Column(name = "password_hash", nullable=false) // Maps to "password_hash" column
    private String passwordHash; 

    // A User can author MANY Threads
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Thread> threads;

    // A User can write MANY Comments
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Comment> comments;

    // A User can cast MANY Thread Votes
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ThreadVote> threadVotes;

    // A User can cast MANY Comment Votes
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CommentVote> commentVotes;
}