package com.example.orderservice.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItemDto {

    @NotNull(message = "Product ID không được để trống!")
    private Long productId;

    @Min(value = 1, message = "Số lượng sản phẩm phải lớn hơn 0!")
    private int quantity;

    @NotNull(message = "Giá sản phẩm không được để trống!")
    private BigDecimal price;
}