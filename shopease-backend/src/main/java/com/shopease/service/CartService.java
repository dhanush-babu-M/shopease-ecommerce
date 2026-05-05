package com.shopease.service;

import com.shopease.entity.CartItem;
import com.shopease.entity.Product;
import com.shopease.entity.User;
import com.shopease.repository.CartRepository;
import com.shopease.repository.ProductRepository;
import com.shopease.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    @Autowired private CartRepository cartRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private ProductRepository productRepository;

    private User getUser(String email) {
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found: " + email));
    }

    // ── Add item or increase quantity ─────────────────────────────────────────
    public CartItem addToCart(String email, Long productId, int quantity) {
        User user = getUser(email);
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new RuntimeException("Product not found: " + productId));

        Optional<CartItem> existing = cartRepository.findByUserAndProduct(user, product);
        if (existing.isPresent()) {
            CartItem item = existing.get();
            item.setQuantity(item.getQuantity() + quantity);
            return cartRepository.save(item);
        }

        CartItem newItem = new CartItem();
        newItem.setUser(user);
        newItem.setProduct(product);
        newItem.setQuantity(quantity);
        return cartRepository.save(newItem);
    }

    // ── View cart ─────────────────────────────────────────────────────────────
    public List<CartItem> getCart(String email) {
        User user = getUser(email);
        return cartRepository.findByUser(user);
    }

    // ── Update quantity ───────────────────────────────────────────────────────
    public CartItem updateQuantity(String email, Long productId, int quantity) {
        User user = getUser(email);
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new RuntimeException("Product not found"));
        CartItem item = cartRepository.findByUserAndProduct(user, product)
            .orElseThrow(() -> new RuntimeException("Item not in cart"));
        item.setQuantity(quantity);
        return cartRepository.save(item);
    }

    // ── Remove single item ────────────────────────────────────────────────────
    public void removeFromCart(String email, Long productId) {
        User user = getUser(email);
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new RuntimeException("Product not found"));
        CartItem item = cartRepository.findByUserAndProduct(user, product)
            .orElseThrow(() -> new RuntimeException("Item not in cart"));
        cartRepository.delete(item);
    }

    // ── Clear entire cart ─────────────────────────────────────────────────────
    @Transactional
    public void clearCart(String email) {
        User user = getUser(email);
        cartRepository.deleteByUser(user);
    }
}
