package org.example.springbootiocdibeantransactionorm.repository;

import org.example.springbootiocdibeantransactionorm.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // Fetch Product cùng với Category để tránh LazyInitializationException
    @Query("SELECT p FROM Product p JOIN FETCH p.category WHERE p.id = :id")
    Optional<Product> findByIdWithCategory(@Param("id") Long id);

    // Fetch tất cả Product của một Category
    @Query("SELECT p FROM Product p WHERE p.category.id = :categoryId")
    List<Product> findAllByCategoryId(@Param("categoryId") Long categoryId);
}
