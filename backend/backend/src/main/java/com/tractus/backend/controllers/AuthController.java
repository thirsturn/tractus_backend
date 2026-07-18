package com.tractus.backend.controllers;

import com.tractus.backend.dtos.AuthRequest;
import com.tractus.backend.dtos.AuthResponse;
import com.tractus.backend.dtos.UserCreateRequest;
import com.tractus.backend.dtos.UserResponse;
import com.tractus.backend.security.CustomUserDetails;
import com.tractus.backend.security.CustomUserDetailsService;
import com.tractus.backend.security.JwtUtil;
import com.tractus.backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthRequest authRequest) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new Exception("Incorrect username or password", e);
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getUsername());
        final String jwt = jwtUtil.generateToken(userDetails);
        
        // Find the user to return in the response
        UserResponse userResponse = userService.getUserByUsername(authRequest.getUsername())
            .orElseThrow(() -> new Exception("User not found"));

        return ResponseEntity.ok(new AuthResponse(jwt, userResponse));
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserCreateRequest request) {
        // Delegate to UserService to create the user and hash the password
        UserResponse response = userService.createUser(request);
        return ResponseEntity.ok(response);
    }
}
