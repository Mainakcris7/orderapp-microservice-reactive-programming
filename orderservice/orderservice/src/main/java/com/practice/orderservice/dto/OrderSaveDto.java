package com.practice.orderservice.dto;

import lombok.Data;

// DTO to save a new order
@Data
public class OrderSaveDto {
    private Integer customerId;
    private Integer productId;
    private int quantity;

}
