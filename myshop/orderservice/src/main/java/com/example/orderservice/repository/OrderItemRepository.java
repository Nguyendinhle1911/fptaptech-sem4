package com.example.orderservice.repository;

import com.example.orderservice.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    /** Lấy danh sách OrderItem theo orderId */
    List<OrderItem> findByOrderId(Long orderId);

    /** Xóa toàn bộ OrderItem theo orderId (nếu cần) */
    void deleteByOrderId(Long orderId);
}
