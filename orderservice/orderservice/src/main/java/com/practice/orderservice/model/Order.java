package com.practice.orderservice.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

// DAO class that represent "orders" table in the DB
@Data
@Table(name = "orders")
public class Order {
    @Id
    @Column(value = "order_id")
    private Integer orderId;

    @Column(value = "product_id")
    private Integer productId;

    @Column(value = "customer_id")
    private Integer customerId;

    @Column(value = "quantity")
    private int quantity;

    @Column(value = "total_price")
    private int totalPrice;

    @Column(value = "order_time")
    private LocalDateTime orderTime;
}
