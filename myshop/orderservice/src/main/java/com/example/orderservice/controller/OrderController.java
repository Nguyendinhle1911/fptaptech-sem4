package com.example.orderservice.controller;

import com.example.orderservice.dto.OrderRequestDto;
import com.example.orderservice.entity.Order;
import com.example.orderservice.entity.OrderItem;
import com.example.orderservice.entity.OrderStatus;
import com.example.orderservice.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    /** 🛒 Tạo đơn hàng mới */
    @PostMapping
    public ResponseEntity<Order> createOrder(@Valid @RequestBody OrderRequestDto orderRequest) {
        Order createdOrder = orderService.createOrder(orderRequest);
        return ResponseEntity.ok(createdOrder);
    }

    /** 📜 Lấy danh sách đơn hàng của user */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Order>> getOrdersByUser(@PathVariable Long userId) {
        List<Order> orders = orderService.getOrdersByUser(userId);
        return ResponseEntity.ok(orders);
    }

    /** 👀 Lấy chi tiết đơn hàng */
    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long orderId) {
        try {
            Order order = orderService.getOrderById(orderId);
            return ResponseEntity.ok(order);
        } catch (Exception e) {
            return ResponseEntity.notFound().build(); // Trả về 404 nếu không tìm thấy đơn hàng
        }
    }

    /** 📝 Lấy danh sách tất cả đơn hàng */
    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        List<Order> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    /** ❌ Hủy đơn hàng */
    @PutMapping("/{orderId}/cancel")
    public ResponseEntity<String> cancelOrder(@PathVariable Long orderId) {
        orderService.cancelOrder(orderId);
        return ResponseEntity.ok("Đơn hàng #" + orderId + " đã bị hủy.");
    }

    /** ✅ Xác nhận đơn hàng */
    @PutMapping("/{orderId}/confirm")
    public ResponseEntity<String> confirmOrder(@PathVariable Long orderId) {
        orderService.confirmOrder(orderId);
        return ResponseEntity.ok("Đơn hàng #" + orderId + " đã được xác nhận.");
    }

    /** 🚚 Cập nhật trạng thái đơn hàng */
    @PutMapping("/{orderId}/status/{status}")
    public ResponseEntity<String> updateOrderStatus(@PathVariable Long orderId, @PathVariable OrderStatus status) {
        try {
            orderService.updateOrderStatus(orderId, status);
            return ResponseEntity.ok("Trạng thái đơn hàng #" + orderId + " đã được cập nhật thành " + status);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Không thể cập nhật trạng thái cho đơn hàng #" + orderId + ": " + e.getMessage());
        }
    }

    /** 🗑️ Xóa đơn hàng */
    @DeleteMapping("/{orderId}")
    public ResponseEntity<String> deleteOrder(@PathVariable Long orderId) {
        try {
            orderService.deleteOrder(orderId);
            return ResponseEntity.ok("Đơn hàng #" + orderId + " đã bị xóa.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Không thể xóa đơn hàng #" + orderId + ": " + e.getMessage());
        }
    }

    /** 🔄 Cập nhật sản phẩm trong đơn hàng */
    @PutMapping("/{orderId}/updateItem/{itemId}")
    public ResponseEntity<String> updateOrderItem(@PathVariable Long orderId, @PathVariable Long itemId, @RequestBody OrderItem newItem) {
        try {
            orderService.updateOrderItem(orderId, itemId, newItem);
            return ResponseEntity.ok("Sản phẩm trong đơn hàng #" + orderId + " đã được cập nhật.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Không thể cập nhật sản phẩm trong đơn hàng #" + orderId + ": " + e.getMessage());
        }
    }
}
