package org.example.productservice.controller;


import org.example.productservice.entity.Product;
import org.example.productservice.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@Validated
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // ✅ Lấy danh sách tất cả sản phẩm
    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @PreAuthorize("hasRole('ADMIN')")
    // ✅ Thêm sản phẩm mới
    @PostMapping
    public ResponseEntity<Product> addProduct(@Valid @RequestBody Product product) {
        return ResponseEntity.ok(productService.addProduct(product));
    }

    // ✅ Cập nhật tồn kho sản phẩm
    @PutMapping("/{productId}/updateStock/{quantity}")
    public ResponseEntity<Product> updateStock(
            @PathVariable @NotNull(message = "Product ID cannot be null") Long productId,
            @PathVariable @Min(value = 1, message = "Quantity must be at least 1") int quantity) {
        return ResponseEntity.ok(productService.updateStock(productId, quantity));
    }

    // ✅ Lấy sản phẩm theo ID
    @GetMapping("/{productId}")
    public ResponseEntity<Product> getProductById(
            @PathVariable @NotNull(message = "Product ID cannot be null") Long productId) {
        return ResponseEntity.ok(productService.getProductById(productId));
    }

    // ✅ Lấy sản phẩm theo tên
    @GetMapping("/name/{productName}")
    public ResponseEntity<Product> getProductByName(
            @PathVariable @NotBlank(message = "Product name cannot be blank") String productName) {
        return ResponseEntity.ok(productService.getProductByName(productName));
    }

    // ✅ Cập nhật thông tin sản phẩm
    @PutMapping("/{productId}")
    public ResponseEntity<Product> updateProduct(
            @PathVariable @NotNull(message = "Product ID cannot be null") Long productId,
            @Valid @RequestBody Product updatedProduct) {
        return ResponseEntity.ok(productService.updateProduct(productId, updatedProduct));
    }

    // ✅ Kiểm tra sản phẩm có đủ hàng không
    @GetMapping("/{productId}/checkStock/{quantity}")
    public ResponseEntity<Boolean> checkStock(
            @PathVariable @NotNull(message = "Product ID cannot be null") Long productId,
            @PathVariable @Min(value = 1, message = "Quantity must be at least 1") int quantity) {
        return ResponseEntity.ok(productService.isProductInStock(productId, quantity));
    }

    @PreAuthorize("hasRole('ADMIN')")
    // ✅ Xóa sản phẩm
    @DeleteMapping("/{productId}")
    public ResponseEntity<String> deleteProduct(
            @PathVariable @NotNull(message = "Product ID cannot be null") Long productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.ok("Product deleted successfully");
    }
}