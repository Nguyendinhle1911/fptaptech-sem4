package org.example.springbootiocdibeantransactionorm.repository;

import org.example.springbootiocdibeantransactionorm.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    // Fetch Order cùng với Customer để tránh LazyInitializationException
    @Query("SELECT o FROM Order o JOIN FETCH o.customer WHERE o.id = :id")
    Optional<Order> findByIdWithCustomer(@Param("id") Long id);

    // Fetch Order cùng với OrderItem để tránh LazyInitializationException
    @Query("SELECT o FROM Order o JOIN FETCH o.orderItems WHERE o.id = :id")
    Optional<Order> findByIdWithOrderItems(@Param("id") Long id);

    // Fetch Order cùng với cả Customer và OrderItem
    @Query("SELECT o FROM Order o JOIN FETCH o.customer JOIN FETCH o.orderItems WHERE o.id = :id")
    Optional<Order> findByIdWithDetails(@Param("id") Long id);

    // Fetch tất cả Order của một Customer
    @Query("SELECT o FROM Order o WHERE o.customer.id = :customerId")
    List<Order> findAllByCustomerId(@Param("customerId") Long customerId);
}
