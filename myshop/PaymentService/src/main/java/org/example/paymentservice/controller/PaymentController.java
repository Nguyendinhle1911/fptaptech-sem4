package org.example.paymentservice.controller;

import org.example.paymentservice.dto.PaymentRequestDto;
import org.example.paymentservice.entity.Payment;
import org.example.paymentservice.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/paypal/process")
    public ResponseEntity<String> processPaypalPayment(@RequestBody PaymentRequestDto requestDto) {
        String paypalOrderId = paymentService.processPaypalPayment(requestDto.getOrderId(), requestDto.getAmount());
        if (paypalOrderId == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create PayPal order");
        }
        return ResponseEntity.ok(paypalOrderId); // Trả về chuỗi trực tiếp
    }

    @PostMapping("/paypal/update-status")
    public ResponseEntity<Void> updatePaymentStatus(@RequestBody Map<String, String> request) {
        Long orderId = Long.valueOf(request.get("orderId"));
        String status = request.get("status");
        String transactionId = request.get("transactionId");
        paymentService.updatePaymentStatus(orderId, status, transactionId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/get-all")
    @PreAuthorize("hasRole('ADMIN')") // Chỉ cho phép ADMIN truy cập
    public List<Payment> getAllPayments() {
        return paymentService.getAllPayments();
    }
}