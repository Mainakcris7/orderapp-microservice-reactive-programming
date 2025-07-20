package com.practice.orderservice.dto;

import lombok.Data;

// DTO to save a new product
@Data
public class ProductSaveDto {
    private String productName;
    private int quantity;
    private int price;
}
