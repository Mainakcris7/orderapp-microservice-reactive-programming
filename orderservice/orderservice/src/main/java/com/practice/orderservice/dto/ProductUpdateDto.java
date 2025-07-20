package com.practice.orderservice.dto;

import lombok.Data;

// DTO to update a product
@Data
public class ProductUpdateDto {
    private Integer productId;
    private String productName;
    private int quantity;
    private int price;
}
