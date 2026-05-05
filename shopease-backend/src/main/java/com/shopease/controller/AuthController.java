package com.shopease.controller;

import com.shopease.dto.AuthDTOs;
import com.shopease.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

/**
 * Auth REST API — all endpoints are PUBLIC (no token required)
 *
 * POST /api/auth/register  → create account, returns JWT
 * POST /api/auth/login     → login, returns JWT
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    // ── Register ──────────────────────────────────────────────────────────────
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody AuthDTOs.RegisterRequest request) {
        try {
            AuthDTOs.AuthResponse response = authService.register(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(Map.of("error", e.getMessage()));
        }
    }

    // ── Login ─────────────────────────────────────────────────────────────────
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody AuthDTOs.LoginRequest request) {
        try {
            AuthDTOs.AuthResponse response = authService.login(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", "Invalid email or password"));
        }
    }
}
