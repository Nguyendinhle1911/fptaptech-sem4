package org.example.orderservice.repository;

import org.example.orderservice.entity.Order;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    @EntityGraph(attributePaths = {"products"})
    @Override
    List<Order> findAll();

    @EntityGraph(attributePaths = {"products"})
    Optional<Order> findById(Long orderId);

    @EntityGraph(attributePaths = {"products"})
    List<Order> findByUserId(Long userId);
}
