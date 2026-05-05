package com.shopease.controller;

import com.shopease.entity.Order;
import com.shopease.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

/**
 * Order API
 *
 * POST /api/orders/place?address=...  → Place order from cart (🔒 JWT)
 * GET  /api/orders/my                 → My order history    (🔒 JWT)
 * GET  /api/orders/all                → All orders          (🔒 Admin JWT)
 * PUT  /api/orders/{id}/status        → Update status       (🔒 Admin JWT)
 */
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/place")
    public ResponseEntity<?> placeOrder(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam String address) {
        try {
            Order order = orderService.placeOrder(userDetails.getUsername(), address);
            return ResponseEntity.ok(order);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/my")
    public ResponseEntity<List<Order>> getMyOrders(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(orderService.getMyOrders(userDetails.getUsername()));
    }

    @GetMapping("/all")
    public ResponseEntity<List<Order>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        try {
            return ResponseEntity.ok(orderService.updateStatus(id, status));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
