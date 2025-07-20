package com.practice.customerservice.dto;

import lombok.Data;

// DTO to add a new order to an existing order
@Data
public class AddCustomerOrderDto {
    private Integer customerId;
    private Integer orderId;
    private String productName;
    private int quantity;
    private int totalPrice;
}
