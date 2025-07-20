package com.practice.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

// DTO for adding a new order to an existing customer
// Encapsulating more details in it for sending notification with less HTTP calls.
@Data
@AllArgsConstructor
public class AddCustomerOrderDto {
    private Integer customerId;
    private Integer orderId;
    private String productName;
    private int quantity;
    private int totalPrice;
}
