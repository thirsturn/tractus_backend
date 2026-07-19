package com.tractus.backend.security;

import com.tractus.backend.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private JwtUtil jwtUtil;
    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setPasswordHash("hashedpassword");
        user.setEmail("test@example.com");
        userDetails = new CustomUserDetails(user);
    }

    @Test
    void testGenerateToken() {
        String token = jwtUtil.generateToken(userDetails);
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void testExtractUsername() {
        String token = jwtUtil.generateToken(userDetails);
        String username = jwtUtil.extractUsername(token);
        assertEquals("testuser", username);
    }

    @Test
    void testValidateToken_ValidToken() {
        String token = jwtUtil.generateToken(userDetails);
        assertTrue(jwtUtil.validateToken(token, userDetails));
    }

    @Test
    void testValidateToken_InvalidUser() {
        String token = jwtUtil.generateToken(userDetails);

        User otherUser = new User();
        otherUser.setId(2L);
        otherUser.setUsername("otheruser");
        otherUser.setPasswordHash("hash");
        otherUser.setEmail("other@example.com");
        UserDetails otherUserDetails = new CustomUserDetails(otherUser);

        assertFalse(jwtUtil.validateToken(token, otherUserDetails));
    }

    @Test
    void testExtractExpiration() {
        String token = jwtUtil.generateToken(userDetails);
        Date expiration = jwtUtil.extractExpiration(token);
        assertNotNull(expiration);
        assertTrue(expiration.after(new Date())); // Expiration should be in the future
    }
}
