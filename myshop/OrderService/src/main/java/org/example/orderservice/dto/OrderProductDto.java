package org.example.orderservice.dto;

import lombok.Data;

@Data
public class OrderProductDto {
    private Long productId;
    private int quantity;
}