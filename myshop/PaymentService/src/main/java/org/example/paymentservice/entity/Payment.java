package org.example.paymentservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "payments")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long orderId; // Liên kết với Order từ OrderService

    @Column(nullable = false)
    private double amount; // Số tiền thanh toán

    @Column(nullable = false)
    private String paymentMethod; // "PAYPAL" hoặc có thể mở rộng thêm phương thức khác

    @Column(nullable = false)
    private String status; // "PENDING", "COMPLETED", "FAILED"

    @Column(nullable = false)
    private LocalDateTime paymentDate; // Thời gian thực hiện thanh toán

    private String transactionId; // ID giao dịch từ PayPal (nếu có)
}