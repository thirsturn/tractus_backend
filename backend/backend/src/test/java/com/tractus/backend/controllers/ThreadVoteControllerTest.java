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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.hasSize;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ThreadVoteControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ThreadVoteRepository threadVoteRepository;
    @Autowired
    private ThreadRepository threadRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SpaceRepository spaceRepository;
    @Autowired
    private ObjectMapper objectMapper;

    private User testUser;
    private Thread testThread;

    @BeforeEach
    void setUp() {
        threadVoteRepository.deleteAll();
        threadRepository.deleteAll();
        userRepository.deleteAll();
        spaceRepository.deleteAll();

        User user = new User();
        user.setUsername("voteUser");
        user.setEmail("vote@test.com");
        user.setPasswordHash("hash");
        testUser = userRepository.save(user);

        Space space = new Space();
        space.setName("VoteSpace");
        space.setDescription("Space for votes");
        space = spaceRepository.save(space);

        Thread thread = new Thread();
        thread.setTitle("Vote Thread");
        thread.setUser(testUser);
        thread.setSpace(space);
        testThread = threadRepository.save(thread);
    }

    @Test
    void testCastVote() throws Exception {
        VoteRequest request = new VoteRequest();
        request.setVoteType(VoteType.UP);
        request.setUserId(testUser.getId());
        request.setTargetId(testThread.getId());

        mockMvc.perform(post("/api/thread-votes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.voteType", is("UP")))
                .andExpect(jsonPath("$.userId", is(testUser.getId().intValue())))
                .andExpect(jsonPath("$.targetId", is(testThread.getId().intValue())));
    }

    @Test
    void testGetVotesByThread() throws Exception {
        ThreadVote vote = new ThreadVote();
        vote.setVoteType(VoteType.DOWN);
        vote.setUser(testUser);
        vote.setThread(testThread);
        threadVoteRepository.save(vote);

        mockMvc.perform(get("/api/thread-votes/thread/" + testThread.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].voteType", is("DOWN")));
    }
}
