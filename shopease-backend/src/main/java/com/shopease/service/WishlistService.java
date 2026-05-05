package com.shopease.service;

import com.shopease.entity.Product;
import com.shopease.entity.User;
import com.shopease.entity.WishlistItem;
import com.shopease.repository.ProductRepository;
import com.shopease.repository.UserRepository;
import com.shopease.repository.WishlistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class WishlistService {

    @Autowired private WishlistRepository wishlistRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private ProductRepository productRepository;

    private User getUser(String email) {
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found: " + email));
    }

    public WishlistItem addToWishlist(String email, Long productId) {
        User user = getUser(email);
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new RuntimeException("Product not found: " + productId));

        if (wishlistRepository.existsByUserAndProduct(user, product)) {
            throw new RuntimeException("Product already in wishlist");
        }

        WishlistItem item = new WishlistItem();
        item.setUser(user);
        item.setProduct(product);
        return wishlistRepository.save(item);
    }

    public List<WishlistItem> getWishlist(String email) {
        return wishlistRepository.findByUser(getUser(email));
    }

    public void removeFromWishlist(String email, Long productId) {
        User user = getUser(email);
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new RuntimeException("Product not found"));
        WishlistItem item = wishlistRepository.findByUserAndProduct(user, product)
            .orElseThrow(() -> new RuntimeException("Item not in wishlist"));
        wishlistRepository.delete(item);
    }
}
