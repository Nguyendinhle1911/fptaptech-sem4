package com.example.orderservice.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum OrderStatus {
    PENDING("Đơn hàng vừa tạo, chờ xác nhận"),
    CONFIRMED("Đã xác nhận, chuẩn bị giao hàng"),
    SHIPPED("Đang vận chuyển"),
    DELIVERED("Đã giao thành công"),
    CANCELED("Đơn hàng bị hủy");

    private final String description;

    OrderStatus(String description) {
        this.description = description;
    }

    @JsonValue
    public String getDescription() {
        return description;
    }

    @JsonCreator
    public static OrderStatus fromString(String value) {
        for (OrderStatus status : OrderStatus.values()) {
            if (status.name().equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Không tìm thấy trạng thái đơn hàng: " + value);
    }
}