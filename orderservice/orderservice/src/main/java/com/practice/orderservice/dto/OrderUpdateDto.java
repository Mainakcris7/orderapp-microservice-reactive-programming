package com.practice.orderservice.dto;

import lombok.Data;

// DTO to update an order
@Data
public class OrderUpdateDto {
    private Integer orderId;
    private Integer quantity;
}
