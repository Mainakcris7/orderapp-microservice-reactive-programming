package com.practice.orderservice.dto;

import lombok.Data;

// DTO for deserializing response from the customer service
@Data
public class CustomerOrderDto {
    private Integer orderId;
    private Integer customerId;
}
