package org.example.paymentservice.service;

import org.example.paymentservice.entity.Payment;

import java.util.List;

public interface PaymentService {
    String processPaypalPayment(Long orderId, double amount);
    void updatePaymentStatus(Long orderId, String status, String transactionId);
    List<Payment> getAllPayments();
}