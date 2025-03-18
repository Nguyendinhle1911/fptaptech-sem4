package org.example.my_shop.repository;

import org.example.my_shop.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product, Long> {
    // Search by name containing keyword
    Page<Product> findByNameContainingIgnoreCase(String keyword, Pageable pageable);

    // Find products within a price range
    Page<Product> findByPriceBetween(Double minPrice, Double maxPrice, Pageable pageable);

    // Custom query to search by name or description
    @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(p.description) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<Product> searchProducts(@Param("search") String search, Pageable pageable);
}
