package com.practice.orderservice.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

// DAO class that represent "products" table in the DB
@Data
@Table(value = "products")
public class Product {
    @Id
    @Column(value = "product_id")
    private Integer productId;

    @Column(value = "product_name")
    private String productName;

    @Column(value = "stock_quantity")
    private int stockQuantity;

    @Column(value = "price")
    private int price;
}
