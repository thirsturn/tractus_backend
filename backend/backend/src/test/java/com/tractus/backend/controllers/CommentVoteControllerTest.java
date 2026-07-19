package com.tractus.backend.controllers;

import tools.jackson.databind.ObjectMapper;
import com.tractus.backend.dtos.VoteRequest;
import com.tractus.backend.models.*;
import com.tractus.backend.models.Thread;
import com.tractus.backend.repositories.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.tractus.backend.security.JwtUtil;
import com.tractus.backend.security.CustomUserDetails;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.hasSize;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class CommentVoteControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private CommentVoteRepository commentVoteRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private ThreadRepository threadRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SpaceRepository spaceRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private JwtUtil jwtUtil;

    private User testUser;
    private Comment testComment;

    @BeforeEach
    void setUp() {
        commentVoteRepository.deleteAll();
        commentRepository.deleteAll();
        threadRepository.deleteAll();
        userRepository.deleteAll();
        spaceRepository.deleteAll();

        User user = new User();
        user.setUsername("commentVoteUser");
        user.setEmail("commentVote@test.com");
        user.setPasswordHash("hash");
        testUser = userRepository.save(user);

        Space space = new Space();
        space.setName("CommentVoteSpace");
        space.setDescription("Space for comment votes");
        space = spaceRepository.save(space);

        Thread thread = new Thread();
        thread.setTitle("Comment Vote Thread");
        thread.setUser(testUser);
        thread.setSpace(space);
        thread = threadRepository.save(thread);

        Comment comment = new Comment();
        comment.setContent("A comment to vote on");
        comment.setUser(testUser);
        comment.setThread(thread);
        testComment = commentRepository.save(comment);
    }

    @Test
    void testCastVote() throws Exception {
        VoteRequest request = new VoteRequest();
        request.setVoteType(VoteType.UP);
        request.setUserId(testUser.getId());
        request.setTargetId(testComment.getId());

        CustomUserDetails userDetails = new CustomUserDetails(testUser);
        String token = jwtUtil.generateToken(userDetails);

        mockMvc.perform(post("/api/comment-votes")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.voteType", is("UP")))
                .andExpect(jsonPath("$.userId", is(testUser.getId().intValue())))
                .andExpect(jsonPath("$.targetId", is(testComment.getId().intValue())));
    }

    @Test
    void testGetVotesByComment() throws Exception {
        CommentVote vote = new CommentVote();
        vote.setVoteType(VoteType.DOWN);
        vote.setUser(testUser);
        vote.setComment(testComment);
        commentVoteRepository.save(vote);

        mockMvc.perform(get("/api/comment-votes/comment/" + testComment.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].voteType", is("DOWN")));
    }
}
