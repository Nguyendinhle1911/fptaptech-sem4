package org.example.springbootiocdibeantransactionorm.service;

import org.example.springbootiocdibeantransactionorm.entity.Category;
import org.example.springbootiocdibeantransactionorm.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    // ✅ Lấy tất cả danh mục (Không fetch products)
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    // ✅ Lấy Category mà không fetch products (tránh query dư thừa nếu không cần)
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
    }

    // ✅ Lấy Category cùng với danh sách Product để tránh LazyInitializationException
    @Transactional
    public Category getCategoryWithProducts(Long id) {
        return categoryRepository.findByIdWithProducts(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
    }
}
