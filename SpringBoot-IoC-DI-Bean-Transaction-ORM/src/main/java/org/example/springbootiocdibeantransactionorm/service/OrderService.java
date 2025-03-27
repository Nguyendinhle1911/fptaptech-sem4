package org.example.springbootiocdibeantransactionorm.service;

import org.example.springbootiocdibeantransactionorm.entity.Order;
import org.example.springbootiocdibeantransactionorm.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderService {
    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    // ✅ Lấy tất cả Order (Không fetch Customer hoặc OrderItems)
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    // ✅ Lấy Order mà không fetch Customer hoặc OrderItems
    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    // ✅ Lấy Order cùng với Customer để tránh LazyInitializationException
    @Transactional
    public Order getOrderWithCustomer(Long id) {
        return orderRepository.findByIdWithCustomer(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    // ✅ Lấy Order cùng với OrderItems để tránh LazyInitializationException
    @Transactional
    public Order getOrderWithOrderItems(Long id) {
        return orderRepository.findByIdWithOrderItems(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    // ✅ Lấy Order cùng với cả Customer và OrderItems trong 1 query
    @Transactional
    public Order getOrderWithDetails(Long id) {
        return orderRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    public Order saveOrder(Order order) {
        return orderRepository.save(order);
    }

    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }
}
