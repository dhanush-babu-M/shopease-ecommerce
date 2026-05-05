package com.shopease.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

/**
 * Health check controller — used to verify the server is running.
 * GET /api/test → should return 200 OK (no auth required)
 */
@RestController
@RequestMapping("/api/test")
public class TestController {

    @GetMapping
    public ResponseEntity<Map<String, String>> healthCheck() {
        return ResponseEntity.ok(Map.of(
            "status", "✅ ShopEase Backend is RUNNING!",
            "version", "1.0.0",
            "port", "8080"
        ));
    }
}
