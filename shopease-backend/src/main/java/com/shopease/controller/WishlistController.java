package com.shopease.controller;

import com.shopease.entity.WishlistItem;
import com.shopease.service.WishlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

/**
 * Wishlist API — all endpoints require JWT
 *
 * POST   /api/wishlist/add/{productId}    → Add to wishlist
 * GET    /api/wishlist                    → View wishlist
 * DELETE /api/wishlist/remove/{productId} → Remove from wishlist
 */
@RestController
@RequestMapping("/api/wishlist")
public class WishlistController {

    @Autowired
    private WishlistService wishlistService;

    @PostMapping("/add/{productId}")
    public ResponseEntity<?> addToWishlist(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long productId) {
        try {
            return ResponseEntity.ok(
                wishlistService.addToWishlist(userDetails.getUsername(), productId));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<List<WishlistItem>> getWishlist(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(wishlistService.getWishlist(userDetails.getUsername()));
    }

    @DeleteMapping("/remove/{productId}")
    public ResponseEntity<Map<String, String>> removeFromWishlist(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long productId) {
        wishlistService.removeFromWishlist(userDetails.getUsername(), productId);
        return ResponseEntity.ok(Map.of("message", "Removed from wishlist"));
    }
}
