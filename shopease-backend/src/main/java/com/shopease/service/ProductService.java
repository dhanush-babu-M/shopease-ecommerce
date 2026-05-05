package com.shopease.service;

import com.shopease.entity.Product;
import com.shopease.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    // ── Admin: Add product ─────────────────────────────────────────────────────
    public Product addProduct(Product product) {
        return productRepository.save(product);
    }

    // ── Admin: Update product ──────────────────────────────────────────────────
    public Product updateProduct(Long id, Product updated) {
        Product existing = productRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Product not found: " + id));
        existing.setName(updated.getName());
        existing.setDescription(updated.getDescription());
        existing.setPrice(updated.getPrice());
        existing.setImageUrl(updated.getImageUrl());
        existing.setCategory(updated.getCategory());
        existing.setStock(updated.getStock());
        return productRepository.save(existing);
    }

    // ── Admin: Delete product ──────────────────────────────────────────────────
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    // ── Public: Get all / by category / search ─────────────────────────────────
    public List<Product> getAllProducts(String category) {
        if (category != null && !category.isEmpty()) {
            return productRepository.findByCategory(category);
        }
        return productRepository.findAll();
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Product not found: " + id));
    }

    public List<Product> searchProducts(String query) {
        return productRepository.findByNameContainingIgnoreCase(query);
    }
}
