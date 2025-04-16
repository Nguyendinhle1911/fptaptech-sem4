package org.example.paymentservice.service;

import org.example.paymentservice.entity.Payment;
import org.example.paymentservice.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private PaymentRepository paymentRepository;

    private static final String PAYPAL_SANDBOX_URL = "https://api.sandbox.paypal.com";
    private static final String PAYPAL_AUTH_URL = PAYPAL_SANDBOX_URL + "/v1/oauth2/token";
    private static final String PAYPAL_ORDER_URL = PAYPAL_SANDBOX_URL + "/v2/checkout/orders";

    private String clientId = "Aek0TvvDlldOX570tv2QpF7h6xQBusCCGTpz5WsJm6KxTLx1zCIGHgKUknBtRBWfd3OHQ6w8RLPDiz4n";
    private String clientSecret = "EIVnVgXXjmtpBFMUzdPj_25CV80r0UaHKzzmMqzbaxBAFneh_f9LYh1E3JKO4oejYSPgs7uM_STD545-";

    private String getAccessToken() {
        String auth = clientId + ":" + clientSecret;
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Basic " + encodedAuth);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String body = "grant_type=client_credentials";
        HttpEntity<String> entity = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(
                    PAYPAL_AUTH_URL,
                    HttpMethod.POST,
                    entity,
                    Map.class
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return (String) response.getBody().get("access_token");
            }
        } catch (Exception e) {
            System.err.println("Error fetching PayPal access token: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    private String createPaypalOrder(double amount) {
        String accessToken = getAccessToken();
        if (accessToken == null) {
            System.err.println("Error: Could not obtain PayPal access token.");
            return null;
        }

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Làm tròn amount về 2 chữ số thập phân
        BigDecimal roundedAmount = BigDecimal.valueOf(amount).setScale(2, RoundingMode.HALF_UP);
        String amountString = roundedAmount.toString();

        Map<String, Object> orderRequest = new HashMap<>();
        orderRequest.put("intent", "CAPTURE");
        orderRequest.put("purchase_units", Collections.singletonList(
                Map.of(
                        "amount", Map.of(
                                "currency_code", "USD",
                                "value", amountString // Sử dụng chuỗi đã làm tròn
                        )
                )
        ));

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(orderRequest, headers);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(
                    PAYPAL_ORDER_URL,
                    HttpMethod.POST,
                    entity,
                    Map.class
            );

            if (response.getStatusCode() == HttpStatus.CREATED && response.getBody() != null) {
                return (String) response.getBody().get("id");
            }
        } catch (Exception e) {
            System.err.println("Error creating PayPal order: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    // New method to capture the PayPal payment
    private boolean capturePaypalPayment(String paypalOrderId) {
        String accessToken = getAccessToken();
        if (accessToken == null) {
            System.err.println("Error: Could not obtain PayPal access token for capture.");
            return false;
        }

        String captureUrl = PAYPAL_ORDER_URL + "/" + paypalOrderId + "/capture";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>("", headers); // Empty body for capture

        try {
            ResponseEntity<Map> response = restTemplate.exchange(
                    captureUrl,
                    HttpMethod.POST,
                    entity,
                    Map.class
            );

            if (response.getStatusCode() == HttpStatus.CREATED && response.getBody() != null) {
                String status = (String) response.getBody().get("status");
                return "COMPLETED".equals(status);
            }
        } catch (Exception e) {
            System.err.println("Error capturing PayPal payment: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    @Override
    @Transactional
    public String processPaypalPayment(Long orderId, double amount) {
        // Kiểm tra xem payment đã tồn tại chưa
        Optional<Payment> existingPayment = paymentRepository.findByOrderId(Long.valueOf(orderId));
        Payment payment;

        if (existingPayment.isPresent()) {
            payment = existingPayment.get();
            // Kiểm tra xem payment đã hoàn tất chưa
            if ("COMPLETED".equals(payment.getStatus())) {
                throw new RuntimeException("Payment for order " + orderId + " is already completed.");
            }
        } else {
            // Tạo payment mới nếu chưa tồn tại (trường hợp này nên được gọi khi tạo order, không phải ở đây)
            payment = new Payment();
            payment.setOrderId(orderId);
            payment.setAmount(amount);
            payment.setPaymentMethod("PAYPAL");
            payment.setStatus("PENDING");
            payment.setPaymentDate(LocalDateTime.now());
            paymentRepository.save(payment);
        }

        // Tạo PayPal order ID
        String paypalOrderId = createPaypalOrder(payment.getAmount());
        if (paypalOrderId == null) {
            payment.setStatus("FAILED");
            paymentRepository.save(payment);
            System.err.println("Failed to create PayPal order for Order ID: " + orderId);
            return null;
        }

        payment.setTransactionId(paypalOrderId); // Cập nhật PayPal order ID
        paymentRepository.save(payment);
        return paypalOrderId;
    }

    // New method to complete the payment
    @Transactional
    @Override
    public boolean completePaypalPayment(Long orderId, String paypalOrderId) {
        Payment payment = paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("Payment not found for order ID: " + orderId));

        if ("COMPLETED".equals(payment.getStatus())) {
            return true; // Payment already completed
        }

        if (!paypalOrderId.equals(payment.getTransactionId())) {
            throw new RuntimeException("PayPal Order ID does not match the payment record.");
        }

        boolean captured = capturePaypalPayment(paypalOrderId);
        if (captured) {
            payment.setStatus("COMPLETED");
            payment.setPaymentDate(LocalDateTime.now());
            paymentRepository.save(payment);
            return true;
        } else {
            payment.setStatus("FAILED");
            paymentRepository.save(payment);
            return false;
        }
    }

    @Override
    @Transactional
    public void updatePaymentStatus(Long orderId, String status, String transactionId) {
        Payment payment = paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("Payment not found for order ID: " + orderId));
        payment.setStatus(status);
        payment.setTransactionId(transactionId);
        payment.setPaymentDate(LocalDateTime.now()); // Cập nhật thời gian thanh toán
        paymentRepository.save(payment);
    }

    @Override
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }
}