package org.example.orderservice.service;

import jakarta.servlet.http.HttpServletRequest;
import org.example.orderservice.dto.*;
import org.example.orderservice.entity.Order;
import org.example.orderservice.entity.OrderProduct;
import org.example.orderservice.repository.OrderProductRepository;
import org.example.orderservice.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderProductRepository orderProductRepository;

    @Autowired
    private RestTemplate restTemplate;

    private static final String PRODUCT_SERVICE_URL = "http://localhost:8082/api/products";
    private static final String PAYMENT_SERVICE_URL = "http://localhost:8084/api/payments/paypal/process";

    public List<OrderResponseDto> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .map(this::mapToOrderResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponseDto createOrder(Long userId, OrderRequestDto requestDto, HttpServletRequest request) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID không được null!");
        }

        if (requestDto == null || requestDto.getProductQuantities() == null || requestDto.getProductQuantities().isEmpty()) {
            throw new IllegalArgumentException("Danh sách sản phẩm không được trống!");
        }

        String paymentMethod = requestDto.getPaymentMethod();
        if (!isValidPaymentMethod(paymentMethod)) {
            throw new IllegalArgumentException("Phương thức thanh toán không hợp lệ: " + paymentMethod);
        }

        if (requestDto.getAddress() == null || requestDto.getAddress().trim().isEmpty()) {
            throw new IllegalArgumentException("Địa chỉ giao hàng không được để trống!");
        }

        Order order = new Order();
        order.setUserId(userId);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("PENDING");
        order.setPaymentMethod(paymentMethod);
        order.setAddress(requestDto.getAddress());
        order.setDescription(requestDto.getDescription());

        Set<OrderProduct> orderProducts = new HashSet<>();
        double totalPrice = 0.0;

        for (var entry : requestDto.getProductQuantities().entrySet()) {
            Long productId = entry.getKey();
            int quantity = entry.getValue();

            if (quantity <= 0) {
                throw new IllegalArgumentException("Số lượng sản phẩm " + productId + " phải lớn hơn 0!");
            }

            ProductDto product = restTemplate.getForObject(PRODUCT_SERVICE_URL + "/" + productId, ProductDto.class);
            if (product == null) {
                throw new RuntimeException("Sản phẩm " + productId + " không tồn tại!");
            }
            if (product.getStock() < quantity) {
                throw new RuntimeException("Sản phẩm " + productId + " không đủ số lượng trong kho!");
            }

            OrderProduct orderProduct = new OrderProduct();
            orderProduct.setOrder(order);
            orderProduct.setProductId(productId);
            orderProduct.setQuantity(quantity);
            orderProducts.add(orderProduct);

            totalPrice += product.getPrice() * quantity;
            updateProductStock(productId, product.getStock() - quantity, request);
        }

        order.setProducts(orderProducts);
        order.setTotalPrice(totalPrice);

        Order savedOrder = orderRepository.save(order);

        if ("PAYPAL".equalsIgnoreCase(paymentMethod)) {
            Map<String, Object> paymentRequest = Map.of(
                    "orderId", savedOrder.getId(),
                    "amount", totalPrice
            );

            HttpHeaders headers = new HttpHeaders();
            String authorizationHeader = request.getHeader("Authorization");
            headers.set("Authorization", authorizationHeader);
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(paymentRequest, headers);

            ResponseEntity<String> paypalOrderResponse = restTemplate.exchange(
                    PAYMENT_SERVICE_URL,
                    HttpMethod.POST,
                    entity,
                    String.class
            );

            if (paypalOrderResponse.getStatusCode() != HttpStatus.OK || paypalOrderResponse.getBody() == null) {
                throw new RuntimeException("Failed to initiate PayPal payment: " + paypalOrderResponse.getStatusCode());
            }

            String paypalOrderId = paypalOrderResponse.getBody();
            savedOrder.setStatus("PENDING");
            orderRepository.save(savedOrder);

            // Note: The actual capture happens after user approval, typically in a separate callback
            return mapToOrderResponseDto(savedOrder); // Return with PayPal Order ID for frontend to handle
        }

        return mapToOrderResponseDto(savedOrder);
    }

    public OrderResponseDto getOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng với ID: " + orderId));
        return mapToOrderResponseDto(order);
    }

    @Transactional
    public OrderResponseDto updateOrderStatus(Long orderId, String newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng với ID: " + orderId));

        if (!isValidStatus(newStatus)) {
            throw new IllegalArgumentException("Trạng thái không hợp lệ: " + newStatus);
        }

        order.setStatus(newStatus);
        Order updatedOrder = orderRepository.save(order);
        return mapToOrderResponseDto(updatedOrder);
    }

    @Transactional
    public OrderResponseDto cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng với ID: " + orderId));

        if (!order.getStatus().equals("PENDING")) {
            throw new IllegalStateException("Chỉ có thể hủy đơn hàng ở trạng thái PENDING!");
        }

        order.setStatus("CANCELLED");
        for (OrderProduct orderProduct : order.getProducts()) {
            ProductDto product = restTemplate.getForObject(PRODUCT_SERVICE_URL + "/" + orderProduct.getProductId(), ProductDto.class);
            if (product != null) {
                // Truyền null cho request vì cancelOrder không được gọi từ controller trực tiếp
                updateProductStock(orderProduct.getProductId(), product.getStock() + orderProduct.getQuantity(), null);
            }
        }

        Order cancelledOrder = orderRepository.save(order);
        return mapToOrderResponseDto(cancelledOrder);
    }

    public List<OrderResponseDto> getOrdersByUserId(Long userId) {
        List<Order> orders = orderRepository.findByUserId(userId);
        return orders.stream()
                .map(this::mapToOrderResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng với ID: " + orderId));

        if (!order.getStatus().equals("PENDING")) {
            throw new IllegalStateException("Chỉ có thể xóa đơn hàng ở trạng thái PENDING!");
        }

        orderRepository.delete(order); // Cascade sẽ xóa các OrderProduct liên quan
    }

    private void updateProductStock(Long productId, int newStock, HttpServletRequest request) {
        ProductDto currentProduct = restTemplate.getForObject(PRODUCT_SERVICE_URL + "/" + productId, ProductDto.class);
        if (currentProduct == null) {
            throw new RuntimeException("Không tìm thấy sản phẩm với ID: " + productId);
        }

        currentProduct.setStock(newStock);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        if (request != null) {
            String authorizationHeader = request.getHeader("Authorization");
            if (authorizationHeader != null) {
                headers.set("Authorization", authorizationHeader);
            }
        }

        HttpEntity<ProductDto> entity = new HttpEntity<>(currentProduct, headers);

        try {
            restTemplate.exchange(
                    PRODUCT_SERVICE_URL + "/" + productId,
                    HttpMethod.PUT,
                    entity,
                    Void.class
            );
        } catch (HttpClientErrorException e) {
            throw new RuntimeException("Lỗi khi cập nhật kho sản phẩm " + productId + ": " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
        } catch (Exception e) {
            throw new RuntimeException("Không thể cập nhật kho sản phẩm " + productId + ": " + e.getMessage());
        }
    }

    private OrderResponseDto mapToOrderResponseDto(Order order) {
        OrderResponseDto responseDto = new OrderResponseDto();
        responseDto.setId(order.getId());
        responseDto.setUserId(order.getUserId());
        responseDto.setTotalPrice(order.getTotalPrice());
        responseDto.setOrderDate(order.getOrderDate());
        responseDto.setStatus(order.getStatus());
        responseDto.setAddress(order.getAddress());
        responseDto.setDescription(order.getDescription());
        responseDto.setPaymentMethod(order.getPaymentMethod());

        List<OrderProductDto> productDtos = order.getProducts().stream()
                .map(op -> {
                    OrderProductDto dto = new OrderProductDto();
                    dto.setProductId(op.getProductId());
                    dto.setQuantity(op.getQuantity());
                    return dto;
                })
                .collect(Collectors.toList());
        responseDto.setProducts(productDtos);

        return responseDto;
    }

    private boolean isValidStatus(String status) {
        return status != null && List.of("PENDING", "COMPLETED", "CANCELLED").contains(status.toUpperCase());
    }

    private boolean isValidPaymentMethod(String paymentMethod) {
        return paymentMethod != null && List.of("CASH", "PAYPAL").contains(paymentMethod.toUpperCase());
    }

}