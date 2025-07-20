package com.practice.notificationservice.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

// The message will get deserialized to it
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
