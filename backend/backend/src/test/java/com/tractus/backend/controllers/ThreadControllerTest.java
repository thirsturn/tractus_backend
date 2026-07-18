package com.tractus.backend.controllers;

import tools.jackson.databind.ObjectMapper;
import com.tractus.backend.dtos.ThreadCreateRequest;
import com.tractus.backend.models.Space;
import com.tractus.backend.models.Thread;
import com.tractus.backend.models.User;
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
public class ThreadControllerTest {

    @Autowired
    private MockMvc mockMvc;
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
    private Space testSpace;

    @BeforeEach
    void setUp() {
        threadRepository.deleteAll();
        userRepository.deleteAll();
        spaceRepository.deleteAll();

        User user = new User();
        user.setUsername("threadUser");
        user.setEmail("thread@test.com");
        user.setPasswordHash("hash");
        testUser = userRepository.save(user);

        Space space = new Space();
        space.setName("ThreadSpace");
        space.setDescription("Space for threads");
        testSpace = spaceRepository.save(space);
    }

    @Test
    void testCreateThread() throws Exception {
        ThreadCreateRequest request = new ThreadCreateRequest();
        request.setTitle("My First Thread");
        request.setUserId(testUser.getId());
        request.setSpaceId(testSpace.getId());

        CustomUserDetails userDetails = new CustomUserDetails(testUser);
        String token = jwtUtil.generateToken(userDetails);

        mockMvc.perform(post("/api/threads")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("My First Thread")))
                .andExpect(jsonPath("$.author.username", is("threadUser")))
                .andExpect(jsonPath("$.spaceId", is(testSpace.getId().intValue())));
    }

    @Test
    void testGetThreadsBySpace() throws Exception {
        Thread thread = new Thread();
        thread.setTitle("Existing Thread");
        thread.setUser(testUser);
        thread.setSpace(testSpace);
        threadRepository.save(thread);

        mockMvc.perform(get("/api/threads/space/" + testSpace.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title", is("Existing Thread")));
    }
}
