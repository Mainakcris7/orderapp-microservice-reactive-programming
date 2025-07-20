package com.practice.authservice.dto;

import com.practice.authservice.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerUpdateDto {
    private Integer customerId;
    private String name;
    private String email;
    private String password;
    private Gender gender;
}
