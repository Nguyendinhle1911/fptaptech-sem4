package com.example.orderservice.repository; // Đúng convention (chữ thường)

import com.example.orderservice.entity.Order;
import com.example.orderservice.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    /** Lấy danh sách đơn hàng theo userId */
    List<Order> findByUserId(Long userId);

    /** Lấy danh sách đơn hàng theo userId & trạng thái */
    List<Order> findByUserIdAndStatus(Long userId, OrderStatus status);

    /** Tìm đơn hàng mới nhất của user */
    Optional<Order> findTopByUserIdOrderByOrderDateDesc(Long userId);
}


