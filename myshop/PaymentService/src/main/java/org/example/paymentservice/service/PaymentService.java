package org.example.paymentservice.service;

import org.example.paymentservice.entity.Payment;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface PaymentService {
    String processPaypalPayment(Long orderId, double amount);

    // New method to complete the payment
    boolean completePaypalPayment(Long orderId, String paypalOrderId); // New method

    void updatePaymentStatus(Long orderId, String status, String transactionId);
    List<Payment> getAllPayments();
}