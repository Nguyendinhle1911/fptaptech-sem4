package org.example.springbootiocdibeantransactionorm.config;

import org.example.springbootiocdibeantransactionorm.entity.Category;
import org.example.springbootiocdibeantransactionorm.entity.Product;
import org.example.springbootiocdibeantransactionorm.repository.CategoryRepository;
import org.example.springbootiocdibeantransactionorm.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean
    public CommandLineRunner dataLoader(CategoryRepository categoryRepository,
                                        ProductRepository productRepository) {
        return args -> {
            // Kiểm tra xem có dữ liệu chưa, nếu có thì không seed lại nữa
            if (categoryRepository.count() == 0) {
                Category IT = categoryRepository.save(new Category("IT", "Các loại thiết bị cho IT"));
                Category books = categoryRepository.save(new Category("Books", "Sách giấy in"));

                productRepository.save(new Product("macbook", 12.0, "New", IT));
                productRepository.save(new Product("SpringBoot Book", 15.0, "Framework book", books));
            }
        };
    }
}
