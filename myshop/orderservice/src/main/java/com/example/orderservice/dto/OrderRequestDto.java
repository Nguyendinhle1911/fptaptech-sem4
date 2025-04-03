package com.example.orderservice.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class OrderRequestDto {

    @NotNull(message = "User ID không được để trống!")
    private Long userId;

    @NotEmpty(message = "Đơn hàng phải có ít nhất một sản phẩm!")
    private List<OrderItemDto> orderItems;
}