package com.shopease.repository;

import com.shopease.entity.Product;
import com.shopease.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByProductOrderByCreatedAtDesc(Product product);
}
