package com.shopease.controller;

import com.shopease.entity.Product;
import com.shopease.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    // ── PUBLIC ────────────────────────────────────────────────────────────────

    // GET /api/products?category=Electronics
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts(
            @RequestParam(required = false) String category) {
        return ResponseEntity.ok(productService.getAllProducts(category));
    }

    // GET /api/products/{id}
    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(productService.getProductById(id));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", e.getMessage()));
        }
    }

    // GET /api/products/search?q=phone
    @GetMapping("/search")
    public ResponseEntity<List<Product>> searchProducts(@RequestParam String q) {
        return ResponseEntity.ok(productService.searchProducts(q));
    }

    // ── ADMIN ONLY (🔒 JWT Required) ──────────────────────────────────────────

    // POST /api/products
    @PostMapping
    public ResponseEntity<Product> addProduct(@RequestBody Product product) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(productService.addProduct(product));
    }

    // PUT /api/products/{id}
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Long id,
                                           @RequestBody Product product) {
        try {
            return ResponseEntity.ok(productService.updateProduct(id, product));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", e.getMessage()));
        }
    }

    // DELETE /api/products/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok(Map.of("message", "Product deleted successfully"));
    }
}
