package org.example.productservice.service;

import org.example.productservice.entity.Product;
import org.example.productservice.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // ✅ Lấy danh sách tất cả sản phẩm
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // ✅ Thêm sản phẩm mới
    public Product addProduct(Product product) {
        return productRepository.save(product);
    }

    // ✅ Cập nhật tồn kho sản phẩm sau khi có đơn hàng
    public Product updateStock(Long productId, int quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (product.getStock() < quantity) {
            throw new RuntimeException("Not enough stock");
        }

        product.setStock(product.getStock() - quantity);
        return productRepository.save(product);
    }

    // ✅ Tìm sản phẩm theo ID
    public Product getProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    // ✅ Tìm sản phẩm theo tên
    public Product getProductByName(String name) {
        return productRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    // ✅ Cập nhật thông tin sản phẩm (tên, giá, tồn kho)
    public Product updateProduct(Long productId, Product updatedProduct) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        product.setName(updatedProduct.getName());
        product.setPrice(updatedProduct.getPrice());
        product.setStock(updatedProduct.getStock());
        product.setDescription(updatedProduct.getDescription());
        return productRepository.save(product);
    }

    // ✅ Kiểm tra số lượng sản phẩm có sẵn
    public boolean isProductInStock(Long productId, int quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        return product.getStock() >= quantity;
    }

    // ✅ Xóa sản phẩm theo ID
    public void deleteProduct(Long productId) {
        if (!productRepository.existsById(productId)) {
            throw new RuntimeException("Product not found");
        }
        productRepository.deleteById(productId);
    }
}
