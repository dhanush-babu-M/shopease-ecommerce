package com.shopease.service;

import com.shopease.config.CustomUserDetailsService;
import com.shopease.config.JwtUtil;
import com.shopease.dto.AuthDTOs;
import com.shopease.entity.User;
import com.shopease.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    /**
     * Register a new user.
     * - Checks if email already exists
     * - Hashes the password with BCrypt
     * - Saves to DB and returns a JWT token
     */
    public AuthDTOs.AuthResponse register(AuthDTOs.RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered: " + request.getEmail());
        }

        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole("USER");

        userRepository.save(user);

        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
        String token = jwtUtil.generateToken(userDetails);

        return new AuthDTOs.AuthResponse(
            token, user.getEmail(), user.getFirstName(), user.getLastName(), user.getRole());
    }

    /**
     * Login with email + password.
     * - Spring Security validates credentials
     * - Returns a JWT token on success
     */
    public AuthDTOs.AuthResponse login(AuthDTOs.LoginRequest request) {
        // This throws BadCredentialsException if wrong credentials
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        User user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new RuntimeException("User not found"));

        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
        String token = jwtUtil.generateToken(userDetails);

        return new AuthDTOs.AuthResponse(
            token, user.getEmail(), user.getFirstName(), user.getLastName(), user.getRole());
    }
}
