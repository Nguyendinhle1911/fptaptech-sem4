package org.example.productservice.controller;

import org.example.productservice.entity.Product;
import org.example.productservice.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
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

    // ✅ Thêm sản phẩm mới
    @PostMapping
    public ResponseEntity<Product> addProduct(@RequestBody Product product) {
        return ResponseEntity.ok(productService.addProduct(product));
    }

    // ✅ Cập nhật tồn kho sản phẩm
    @PutMapping("/{productId}/updateStock/{quantity}")
    public ResponseEntity<Product> updateStock(@PathVariable Long productId, @PathVariable int quantity) {
        return ResponseEntity.ok(productService.updateStock(productId, quantity));
    }

    // ✅ Lấy sản phẩm theo ID
    @GetMapping("/{productId}")
    public ResponseEntity<Product> getProductById(@PathVariable Long productId) {
        return ResponseEntity.ok(productService.getProductById(productId));
    }

    // ✅ Lấy sản phẩm theo tên
    @GetMapping("/name/{productName}")
    public ResponseEntity<Product> getProductByName(@PathVariable String productName) {
        return ResponseEntity.ok(productService.getProductByName(productName));
    }

    // ✅ Cập nhật thông tin sản phẩm
    @PutMapping("/{productId}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long productId, @RequestBody Product updatedProduct) {
        return ResponseEntity.ok(productService.updateProduct(productId, updatedProduct));
    }

    // ✅ Kiểm tra sản phẩm có đủ hàng không
    @GetMapping("/{productId}/checkStock/{quantity}")
    public ResponseEntity<Boolean> checkStock(@PathVariable Long productId, @PathVariable int quantity) {
        return ResponseEntity.ok(productService.isProductInStock(productId, quantity));
    }

    // ✅ Xóa sản phẩm
    @DeleteMapping("/{productId}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.ok("Product deleted successfully");
    }
}
