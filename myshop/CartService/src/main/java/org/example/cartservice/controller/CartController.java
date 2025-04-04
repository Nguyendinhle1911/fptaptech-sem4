package org.example.cartservice.controller;

import org.example.cartservice.dto.CartItemRequest;
import org.example.cartservice.entity.Cart;
import org.example.cartservice.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    private Long getUserIdFromAuthentication() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof Long) {
            return (Long) principal; // Trả về userId từ token
        }
        throw new IllegalStateException("User ID not found in authentication context");
    }

    @PostMapping("/add")
    public ResponseEntity<Cart> addToCart(@Valid @RequestBody CartItemRequest request) {
        try {
            Long userId = getUserIdFromAuthentication();
            Cart updatedCart = cartService.addToCart(userId, request.getProductId());
            return ResponseEntity.ok(updatedCart);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(401).body(null); // Unauthorized
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(null); // Not Found
        }
    }

    @DeleteMapping("/remove")
    public ResponseEntity<Cart> removeFromCart(@Valid @RequestBody CartItemRequest request) {
        try {
            Long userId = getUserIdFromAuthentication();
            Cart updatedCart = cartService.removeFromCart(userId, request.getProductId());
            return ResponseEntity.ok(updatedCart);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(401).body(null);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(null);
        }
    }

    @GetMapping
    public ResponseEntity<Cart> getCart() {
        try {
            Long userId = getUserIdFromAuthentication();
            Cart cart = cartService.getCartByUserId(userId);
            return ResponseEntity.ok(cart);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(401).body(null);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(null);
        }
    }
}