package com.example.orderservice.service;

import com.example.orderservice.repository.OrderRepository;
import com.example.orderservice.repository.OrderItemRepository;
import com.example.orderservice.dto.OrderRequestDto;
import com.example.orderservice.entity.Order;
import com.example.orderservice.entity.OrderItem;
import com.example.orderservice.entity.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    /** 🛒 Tạo đơn hàng mới */
    @Transactional
    public Order createOrder(OrderRequestDto orderRequest) {
        if (orderRequest.getOrderItems().isEmpty()) {
            throw new IllegalArgumentException("Đơn hàng phải có ít nhất một sản phẩm!");
        }

        Order order = new Order();
        order.setUserId(orderRequest.getUserId());
        order.setStatus(OrderStatus.PENDING);

        // Lưu đơn hàng để có ID
        Order savedOrder = orderRepository.save(order);

        // Tạo danh sách các OrderItem và tính tổng tiền
        List<OrderItem> orderItems = orderRequest.getOrderItems().stream()
                .map(itemDto -> new OrderItem(savedOrder, itemDto.getProductId(), itemDto.getQuantity(), itemDto.getPrice()))
                .collect(Collectors.toList());

        // Lưu tất cả các orderItems vào cơ sở dữ liệu
        orderItemRepository.saveAll(orderItems);

        // Tính tổng tiền với BigDecimal
        BigDecimal totalAmount = orderItems.stream()
                .map(item -> item.getPrice().multiply(new BigDecimal(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Cập nhật tổng tiền vào đơn hàng
        savedOrder.setTotalAmount(totalAmount);

        // Lưu lại đơn hàng với tổng tiền đã cập nhật
        orderRepository.save(savedOrder);

        return savedOrder;
    }

    /** 👤 Lấy danh sách đơn hàng của user */
    public List<Order> getOrdersByUser(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    /** 📝 Lấy danh sách tất cả đơn hàng */
    public List<Order> getAllOrders() {
        return orderRepository.findAll(); // Trả về tất cả đơn hàng
    }

    /** 👀 Lấy chi tiết đơn hàng */
    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng!"));
    }

    /** ❌ Hủy đơn hàng */
    @Transactional
    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng!"));

        if (order.getStatus() == OrderStatus.CANCELED) {
            throw new IllegalStateException("Đơn hàng này đã bị hủy trước đó!");
        }

        order.setStatus(OrderStatus.CANCELED);
        orderRepository.save(order);
    }

    /** ✅ Xác nhận đơn hàng */
    @Transactional
    public void confirmOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng!"));

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new IllegalStateException("Chỉ có thể xác nhận đơn hàng ở trạng thái PENDING!");
        }

        order.setStatus(OrderStatus.CONFIRMED);
        orderRepository.save(order);
    }

    /** 🚚 Cập nhật trạng thái đơn hàng */
    @Transactional
    public void updateOrderStatus(Long orderId, OrderStatus status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng!"));

        order.setStatus(status);
        orderRepository.save(order);
    }

    /** 🗑️ Xóa đơn hàng */
    @Transactional
    public void deleteOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng!"));

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new IllegalStateException("Chỉ có thể xóa đơn hàng khi trạng thái là PENDING!");
        }

        orderRepository.delete(order);
    }

    /** 🔄 Cập nhật sản phẩm trong đơn hàng */
    @Transactional
    public void updateOrderItem(Long orderId, Long itemId, OrderItem newItem) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng!"));

        OrderItem orderItem = orderItemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm trong đơn hàng!"));

        orderItem.setQuantity(newItem.getQuantity());
        orderItem.setPrice(newItem.getPrice());
        orderItemRepository.save(orderItem);
    }
}
