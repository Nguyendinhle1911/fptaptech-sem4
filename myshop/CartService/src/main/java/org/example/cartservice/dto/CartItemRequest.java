package org.example.cartservice.dto;

import jakarta.validation.constraints.NotNull;

public class CartItemRequest {

    @NotNull(message = "Product ID is required")
    private Long productId;

    // Constructors
    public CartItemRequest() {}

    public CartItemRequest(Long productId) {
        this.productId = productId;
    }

    // Getters and Setters
    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }
}