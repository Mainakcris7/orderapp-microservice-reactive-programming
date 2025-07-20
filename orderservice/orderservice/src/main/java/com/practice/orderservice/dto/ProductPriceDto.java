package com.practice.orderservice.dto;

import lombok.Builder;
import lombok.Data;

// DTO to return product price with its name
@Data
@Builder
public class ProductPriceDto {
    private String productName;
    private int totalPrice;
}
