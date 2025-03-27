package org.example.springbootiocdibeantransactionorm.service;

import org.example.springbootiocdibeantransactionorm.entity.OrderItem;
import org.example.springbootiocdibeantransactionorm.repository.OrderItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class OrderItemService {
    private final OrderItemRepository orderItemRepository;

    public OrderItemService(OrderItemRepository orderItemRepository) {
        this.orderItemRepository = orderItemRepository;
    }

    // ✅ Lấy tất cả OrderItem (Không fetch Order hoặc Product)
    public List<OrderItem> getAllOrderItems() {
        return orderItemRepository.findAll();
    }

    // ✅ Lấy OrderItem mà không fetch Order hoặc Product
    public OrderItem getOrderItemById(Long id) {
        return orderItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("OrderItem not found"));
    }

    // ✅ Lấy OrderItem cùng với Order để tránh LazyInitializationException
    public OrderItem getOrderItemWithOrder(Long id) {
        return orderItemRepository.findByIdWithOrder(id)
                .orElseThrow(() -> new RuntimeException("OrderItem not found"));
    }

    // ✅ Lấy OrderItem cùng với Product để tránh LazyInitializationException
    public OrderItem getOrderItemWithProduct(Long id) {
        return orderItemRepository.findByIdWithProduct(id)
                .orElseThrow(() -> new RuntimeException("OrderItem not found"));
    }

    // ✅ Lấy tất cả OrderItem của một Order và fetch Product
    public List<OrderItem> getOrderItemsByOrder(Long orderId) {
        return orderItemRepository.findAllByOrderIdWithProduct(orderId);
    }

    public OrderItem saveOrderItem(OrderItem orderItem) {
        return orderItemRepository.save(orderItem);
    }

    public void deleteOrderItem(Long id) {
        orderItemRepository.deleteById(id);
    }
}
