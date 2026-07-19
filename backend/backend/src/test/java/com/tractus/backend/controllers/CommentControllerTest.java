package com.tractus.backend.controllers;

import tools.jackson.databind.ObjectMapper;
import com.tractus.backend.dtos.CommentCreateRequest;
import com.tractus.backend.models.Comment;
import com.tractus.backend.models.Space;
import com.tractus.backend.models.Thread;
import com.tractus.backend.models.User;
import com.tractus.backend.repositories.CommentRepository;
import com.tractus.backend.repositories.SpaceRepository;
import com.tractus.backend.repositories.ThreadRepository;
import com.tractus.backend.repositories.UserRepository;
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
public class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;
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
    private Thread testThread;

    @BeforeEach
    void setUp() {
        commentRepository.deleteAll();
        threadRepository.deleteAll();
        userRepository.deleteAll();
        spaceRepository.deleteAll();

        User user = new User();
        user.setUsername("commentUser");
        user.setEmail("comment@test.com");
        user.setPasswordHash("hash");
        testUser = userRepository.save(user);

        Space space = new Space();
        space.setName("CommentSpace");
        space.setDescription("Space for comments");
        space = spaceRepository.save(space);

        Thread thread = new Thread();
        thread.setTitle("Comment Thread");
        thread.setUser(testUser);
        thread.setSpace(space);
        testThread = threadRepository.save(thread);
    }

    @Test
    void testCreateComment() throws Exception {
        CommentCreateRequest request = new CommentCreateRequest();
        request.setContent("This is a test comment");
        request.setUserId(testUser.getId());
        request.setThreadId(testThread.getId());

        CustomUserDetails userDetails = new CustomUserDetails(testUser);
        String token = jwtUtil.generateToken(userDetails);

        mockMvc.perform(post("/api/comments")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", is("This is a test comment")))
                .andExpect(jsonPath("$.author.username", is("commentUser")))
                .andExpect(jsonPath("$.threadId", is(testThread.getId().intValue())));
    }

    @Test
    void testGetCommentsByThread() throws Exception {
        Comment comment = new Comment();
        comment.setContent("Existing comment");
        comment.setUser(testUser);
        comment.setThread(testThread);
        commentRepository.save(comment);

        mockMvc.perform(get("/api/comments/thread/" + testThread.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].content", is("Existing comment")));
    }
}
