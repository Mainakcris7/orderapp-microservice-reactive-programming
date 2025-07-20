package com.practice.authservice.dto;

import lombok.Data;

@Data
public class CustomerLoginDto {
    private String email;
    private String password;
}
