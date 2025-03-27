package org.example.springbootiocdibeantransactionorm.repository;

import org.example.springbootiocdibeantransactionorm.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    // Fetch OrderItem cùng với Order để tránh LazyInitializationException
    @Query("SELECT oi FROM OrderItem oi JOIN FETCH oi.order WHERE oi.id = :id")
    Optional<OrderItem> findByIdWithOrder(@Param("id") Long id);

    // Fetch OrderItem cùng với Product để tránh LazyInitializationException
    @Query("SELECT oi FROM OrderItem oi JOIN FETCH oi.product WHERE oi.id = :id")
    Optional<OrderItem> findByIdWithProduct(@Param("id") Long id);

    // Fetch tất cả OrderItem của một Order
    @Query("SELECT oi FROM OrderItem oi JOIN FETCH oi.product WHERE oi.order.id = :orderId")
    List<OrderItem> findAllByOrderIdWithProduct(@Param("orderId") Long orderId);
}
