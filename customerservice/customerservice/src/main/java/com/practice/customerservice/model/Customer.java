package com.practice.customerservice.model;

import com.practice.customerservice.enums.Gender;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

// DAO class to represent "customers" table in DB
@Data
@Table("customers")
public class Customer {
    @Id
    @Column(value = "customer_id")
    private Integer customerId;

    @Column(value = "name")
    private String name;

    @Column(value = "email")
    private String email;

    @Column(value = "password")
    private String password;

    @Column(value = "gender")
    private Gender gender;
}
