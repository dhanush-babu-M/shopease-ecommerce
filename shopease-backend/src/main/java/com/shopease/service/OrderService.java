package com.shopease.service;

import com.shopease.entity.*;
import com.shopease.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    @Autowired private OrderRepository orderRepository;
    @Autowired private CartRepository cartRepository;
    @Autowired private UserRepository userRepository;

    private User getUser(String email) {
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found: " + email));
    }

    // ── Place order from current cart ─────────────────────────────────────────
    @Transactional
    public Order placeOrder(String email, String shippingAddress) {
        User user = getUser(email);
        List<CartItem> cartItems = cartRepository.findByUser(user);

        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty — add items before placing an order");
        }

        Order order = new Order();
        order.setUser(user);
        order.setShippingAddress(shippingAddress);
        order.setStatus("CONFIRMED");

        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (CartItem cartItem : cartItems) {
            OrderItem oi = new OrderItem();
            oi.setOrder(order);
            oi.setProduct(cartItem.getProduct());
            oi.setQuantity(cartItem.getQuantity());
            oi.setPrice(cartItem.getProduct().getPrice());
            orderItems.add(oi);
            total = total.add(cartItem.getProduct().getPrice()
                .multiply(BigDecimal.valueOf(cartItem.getQuantity())));
        }

        order.setItems(orderItems);
        order.setTotalAmount(total);
        Order saved = orderRepository.save(order);

        // Clear cart after order placed
        cartRepository.deleteByUser(user);

        return saved;
    }

    // ── Get my orders ─────────────────────────────────────────────────────────
    public List<Order> getMyOrders(String email) {
        User user = getUser(email);
        return orderRepository.findByUserOrderByCreatedAtDesc(user);
    }

    // ── Admin: Update order status ────────────────────────────────────────────
    public Order updateStatus(Long orderId, String status) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));
        order.setStatus(status);
        return orderRepository.save(order);
    }

    // ── Admin: Get all orders ─────────────────────────────────────────────────
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
}
