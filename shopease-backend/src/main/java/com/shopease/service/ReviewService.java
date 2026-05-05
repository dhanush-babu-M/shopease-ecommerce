package com.shopease.service;

import com.shopease.entity.Product;
import com.shopease.entity.Review;
import com.shopease.entity.User;
import com.shopease.repository.ProductRepository;
import com.shopease.repository.ReviewRepository;
import com.shopease.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ReviewService {

    @Autowired private ReviewRepository reviewRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private ProductRepository productRepository;

    public Review addReview(String email, Long productId, int rating, String comment) {
        if (rating < 1 || rating > 5) {
            throw new RuntimeException("Rating must be between 1 and 5");
        }
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new RuntimeException("Product not found: " + productId));

        Review review = new Review();
        review.setUser(user);
        review.setProduct(product);
        review.setRating(rating);
        review.setComment(comment);
        return reviewRepository.save(review);
    }

    public List<Review> getReviewsByProduct(Long productId) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new RuntimeException("Product not found: " + productId));
        return reviewRepository.findByProductOrderByCreatedAtDesc(product);
    }
}
