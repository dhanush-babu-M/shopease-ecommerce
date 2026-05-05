package com.shopease.controller;

import com.shopease.entity.CartItem;
import com.shopease.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

/**
 * Cart API — all endpoints require JWT token
 *
 * POST   /api/cart/add?productId=1&quantity=2   → Add item
 * GET    /api/cart                               → View cart
 * PUT    /api/cart/update?productId=1&quantity=3 → Update quantity
 * DELETE /api/cart/remove/{productId}            → Remove item
 * DELETE /api/cart/clear                         → Clear entire cart
 */
@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping("/add")
    public ResponseEntity<?> addToCart(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam Long productId,
            @RequestParam(defaultValue = "1") int quantity) {
        try {
            CartItem item = cartService.addToCart(
                userDetails.getUsername(), productId, quantity);
            return ResponseEntity.ok(item);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<List<CartItem>> getCart(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(cartService.getCart(userDetails.getUsername()));
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateQuantity(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam Long productId,
            @RequestParam int quantity) {
        try {
            return ResponseEntity.ok(
                cartService.updateQuantity(userDetails.getUsername(), productId, quantity));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/remove/{productId}")
    public ResponseEntity<Map<String, String>> removeFromCart(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long productId) {
        cartService.removeFromCart(userDetails.getUsername(), productId);
        return ResponseEntity.ok(Map.of("message", "Item removed from cart"));
    }

    @DeleteMapping("/clear")
    public ResponseEntity<Map<String, String>> clearCart(
            @AuthenticationPrincipal UserDetails userDetails) {
        cartService.clearCart(userDetails.getUsername());
        return ResponseEntity.ok(Map.of("message", "Cart cleared"));
    }
}
