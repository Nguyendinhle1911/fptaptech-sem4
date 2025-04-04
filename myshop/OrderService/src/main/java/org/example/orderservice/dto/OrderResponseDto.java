package org.example.orderservice.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class OrderResponseDto {
    private Long id;
    private Long userId;
    private double totalPrice;
    private LocalDateTime orderDate;
    private String status;
    private String paymentMethod; // "CASH" or "PAYPAL"
    private List<OrderProductDto> products;
}