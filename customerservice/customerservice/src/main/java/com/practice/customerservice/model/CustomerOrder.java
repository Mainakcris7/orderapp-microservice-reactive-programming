package com.practice.customerservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

// DAO class to represent "customer_orders" table in DB
// To keep the details of which orders are associated to which customers
@Data
@AllArgsConstructor
@Table("customer_orders")
public class CustomerOrder {
    @Id
    private Integer id;

    @Column(value = "order_id")
    private Integer orderId;

    @Column(value = "customer_id")
    private Integer customerId;
}
