package com.shopease.controller;

import com.shopease.entity.Review;
import com.shopease.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

/**
 * Review API
 *
 * POST /api/reviews?productId=1&rating=5&comment=Great!  → Add review  (🔒 JWT)
 * GET  /api/reviews/product/{productId}                   → Get reviews (Public)
 */
@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @PostMapping
    public ResponseEntity<?> addReview(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam Long productId,
            @RequestParam int rating,
            @RequestParam String comment) {
        try {
            Review review = reviewService.addReview(
                userDetails.getUsername(), productId, rating, comment);
            return ResponseEntity.ok(review);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<Review>> getReviews(@PathVariable Long productId) {
        return ResponseEntity.ok(reviewService.getReviewsByProduct(productId));
    }
}
