package org.example.orderservice.dto;

import lombok.Data;

@Data
public class ProductDto {
    private Long id;
    private String name;
    private double price;
    private int stock;
    private String description;
}