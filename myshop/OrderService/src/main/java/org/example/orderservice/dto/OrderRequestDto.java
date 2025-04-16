package org.example.orderservice.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class OrderRequestDto {
    private Map<Long, Integer> productQuantities; // productId -> quantity
    private String paymentMethod; // "CASH" or "PAYPAL"
    private String address;
    private String description;

}