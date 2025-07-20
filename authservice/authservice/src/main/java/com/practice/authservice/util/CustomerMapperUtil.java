package com.practice.authservice.util;

import com.practice.authservice.dto.CustomerRegisterDto;
import com.practice.authservice.dto.CustomerUpdateDto;
import com.practice.authservice.model.Customer;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CustomerMapperUtil {
    private final PasswordEncoder encoder;

    public Customer fromRegisterDto(CustomerRegisterDto dto){
        Customer customer = new Customer();
        customer.setCustomerId(null);
        customer.setName(dto.getName());
        customer.setEmail(dto.getEmail());
        customer.setPassword(encoder.encode(dto.getPassword()));
        customer.setGender(dto.getGender());

        return customer;
    }

    public Customer fromUpdateDto(CustomerUpdateDto dto){
        Customer customer = new Customer();

        customer.setCustomerId(dto.getCustomerId());
        customer.setName(dto.getName());
        customer.setEmail(dto.getEmail());
        customer.setPassword(encoder.encode(dto.getPassword()));
        customer.setGender(dto.getGender());

        return customer;
    }
}
