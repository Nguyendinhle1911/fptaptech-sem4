package org.example.paymentservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentRequestDto {
    private Long orderId;
    private double amount;
}