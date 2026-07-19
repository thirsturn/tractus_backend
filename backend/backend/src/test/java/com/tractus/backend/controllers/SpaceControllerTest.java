package com.tractus.backend.controllers;

import tools.jackson.databind.ObjectMapper;
import com.tractus.backend.models.Space;
import com.tractus.backend.repositories.SpaceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.context.support.WithMockUser;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.hasSize;
import com.tractus.backend.security.JwtUtil;
import com.tractus.backend.security.CustomUserDetails;
import com.tractus.backend.models.User;
import com.tractus.backend.repositories.UserRepository;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class SpaceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SpaceRepository spaceRepository;
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        spaceRepository.deleteAll();
    }

    @Test
    void testCreateSpace() throws Exception {
        com.tractus.backend.dtos.SpaceCreateRequest request = new com.tractus.backend.dtos.SpaceCreateRequest();
        request.setName("Tech");
        request.setDescription("Tech space");

        User testUser = new User();
        testUser.setUsername("testuser");
        testUser.setEmail("testuser@test.com");
        testUser.setPasswordHash("hash");
        userRepository.save(testUser);

        CustomUserDetails userDetails = new CustomUserDetails(testUser);
        String token = jwtUtil.generateToken(userDetails);

        mockMvc.perform(post("/api/spaces")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Tech")))
                .andExpect(jsonPath("$.description", is("Tech space")));
    }

    @Test
    void testGetAllSpaces() throws Exception {
        Space space = new Space();
        space.setName("Music");
        space.setDescription("Music space");
        spaceRepository.save(space);

        mockMvc.perform(get("/api/spaces"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("Music")));
    }
}
