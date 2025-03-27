package org.example.springbootiocdibeantransactionorm.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;
    private String description;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Product> products = new ArrayList<>();

    // ✅ Constructor chỉ có name & description
    public Category(String name, String description) {
        this.name = name;
        this.description = description;
        this.products = new ArrayList<>(); // Khởi tạo danh sách sản phẩm rỗng để tránh lỗi null
    }
}
