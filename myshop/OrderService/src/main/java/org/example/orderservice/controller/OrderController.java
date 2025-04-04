package org.example.orderservice.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.example.orderservice.dto.OrderRequestDto;
import org.example.orderservice.dto.OrderResponseDto;
import org.example.orderservice.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<Object> getAllOrders() {
        try {
            List<OrderResponseDto> orders = orderService.getAllOrders();
            return new ResponseEntity<>(orders, HttpStatus.OK);
        } catch (RuntimeException e) {
            return buildErrorResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<Object> createOrder(@RequestBody OrderRequestDto requestDto, HttpServletRequest request) {
        try {
            Long userId = getAuthenticatedUserId();
            OrderResponseDto responseDto = orderService.createOrder(userId, requestDto, request); // Truyền request vào service
            return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return buildErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (RuntimeException e) {
            return buildErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<Object> getOrderById(@PathVariable Long orderId) {
        try {
            Long userId = getAuthenticatedUserId();
            OrderResponseDto responseDto = orderService.getOrderById(orderId);
            if (!responseDto.getUserId().equals(userId)) {
                return buildErrorResponse("Bạn không có quyền xem đơn hàng này!", HttpStatus.FORBIDDEN);
            }
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        } catch (RuntimeException e) {
            return buildErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/{orderId}")
    public ResponseEntity<Object> getOrderByIdForAdmin(@PathVariable Long orderId) {
        try {
            OrderResponseDto responseDto = orderService.getOrderById(orderId);
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        } catch (RuntimeException e) {
            return buildErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/admin/{orderId}/status")
    public ResponseEntity<Object> updateOrderStatusForAdmin(@PathVariable Long orderId, @RequestBody Map<String, String> statusRequest) {
        try {
            String newStatus = statusRequest.get("status");
            OrderResponseDto responseDto  = orderService.updateOrderStatus(orderId, newStatus);
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return buildErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (RuntimeException e) {
            return buildErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{orderId}/status")
    public ResponseEntity<Object> updateOrderStatus(@PathVariable Long orderId, @RequestBody Map<String, String> statusRequest) {
        try {
            Long userId = getAuthenticatedUserId();
            OrderResponseDto responseDto = orderService.getOrderById(orderId);
            if (!responseDto.getUserId().equals(userId)) {
                return buildErrorResponse("Bạn không có quyền cập nhật đơn hàng này!", HttpStatus.FORBIDDEN);
            }
            String newStatus = statusRequest.get("status");
            responseDto = orderService.updateOrderStatus(orderId, newStatus);
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return buildErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (RuntimeException e) {
            return buildErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/{orderId}/cancel")
    public ResponseEntity<Object> cancelOrder(@PathVariable Long orderId) {
        try {
            Long userId = getAuthenticatedUserId();
            OrderResponseDto responseDto = orderService.getOrderById(orderId);
            if (!responseDto.getUserId().equals(userId)) {
                return buildErrorResponse("Bạn không có quyền hủy đơn hàng này!", HttpStatus.FORBIDDEN);
            }
            responseDto = orderService.cancelOrder(orderId);
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        } catch (IllegalStateException e) {
            return buildErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (RuntimeException e) {
            return buildErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }


    @GetMapping("/my-orders")
    public ResponseEntity<Object> getMyOrders() {
        try {
            Long userId = getAuthenticatedUserId();
            List<OrderResponseDto> orders = orderService.getOrdersByUserId(userId);
            return new ResponseEntity<>(orders, HttpStatus.OK);
        } catch (SecurityException e) {
            return buildErrorResponse("Không thể xác thực người dùng!", HttpStatus.UNAUTHORIZED);
        }
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<Object> deleteOrder(@PathVariable Long orderId) {
        try {
            Long userId = getAuthenticatedUserId();
            OrderResponseDto responseDto = orderService.getOrderById(orderId);
            if (!responseDto.getUserId().equals(userId)) {
                return buildErrorResponse("Bạn không có quyền xóa đơn hàng này!", HttpStatus.FORBIDDEN);
            }
            orderService.deleteOrder(orderId);
            return new ResponseEntity<>("Đơn hàng đã được xóa thành công!", HttpStatus.OK);
        } catch (IllegalStateException e) {
            return buildErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (RuntimeException e) {
            return buildErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    private Long getAuthenticatedUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal() == null) {
            throw new SecurityException("Người dùng chưa được xác thực!");
        }
        Object principal = authentication.getPrincipal();
        if (principal instanceof Long) {
            return (Long) principal;
        }
        throw new SecurityException("Không thể lấy ID người dùng từ thông tin xác thực!");
    }

    private ResponseEntity<Object> buildErrorResponse(String message, HttpStatus status) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", message);
        errorResponse.put("status", String.valueOf(status.value()));
        return new ResponseEntity<>(errorResponse, status);
    }
}