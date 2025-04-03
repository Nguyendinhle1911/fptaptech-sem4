package com.example.orderservice.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference  // 🚀 NGĂN VÒNG LẶP JSON
    private List<OrderItem> orderItems = new ArrayList<>();

    @Column(nullable = false, updatable = false)
    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    // Thêm trường totalAmount để lưu tổng tiền của đơn hàng
    @Column(nullable = false)
    private BigDecimal totalAmount = BigDecimal.ZERO;

    @PrePersist
    protected void onCreate() {
        this.orderDate = LocalDateTime.now();
        if (this.status == null) {
            this.status = OrderStatus.PENDING;
        }
    }

    // Phương thức tính tổng tiền
    public void calculateTotalAmount() {
        this.totalAmount = orderItems.stream()
                .map(item -> item.getPrice().multiply(new BigDecimal(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add); // Tính tổng tiền
    }

    public void addOrderItem(OrderItem item) {
        if (item != null) {
            orderItems.add(item);
            item.setOrder(this);
            calculateTotalAmount(); // Cập nhật tổng tiền sau khi thêm sản phẩm
        }
    }

    public void removeOrderItem(OrderItem item) {
        if (item != null && orderItems.contains(item)) {
            orderItems.remove(item);
            item.setOrder(null);
            calculateTotalAmount(); // Cập nhật tổng tiền sau khi xóa sản phẩm
        }
    }
}
