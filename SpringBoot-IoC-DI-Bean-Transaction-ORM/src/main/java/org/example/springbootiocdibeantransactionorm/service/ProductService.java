package org.example.springbootiocdibeantransactionorm.service;

import org.example.springbootiocdibeantransactionorm.entity.Product;
import org.example.springbootiocdibeantransactionorm.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // ✅ Lấy tất cả Product (Không fetch Category)
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // ✅ Lấy Product mà không fetch Category
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    // ✅ Lấy Product cùng với Category để tránh LazyInitializationException
    @Transactional
    public Product getProductWithCategory(Long id) {
        return productRepository.findByIdWithCategory(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    @Transactional
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}
