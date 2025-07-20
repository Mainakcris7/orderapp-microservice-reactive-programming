package com.practice.customerservice.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

// DTO to send details with the notification object
@Data
@NoArgsConstructor
public class NotificationDto {
    private String customerName;
    private String email;
    private String orderType;
    private String productName;
    private int productQuantity;
    private int totalPrice;
}
