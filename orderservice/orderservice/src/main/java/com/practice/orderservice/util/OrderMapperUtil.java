package com.practice.orderservice.util;

import com.practice.orderservice.dto.OrderSaveDto;
import com.practice.orderservice.model.Order;

public class OrderMapperUtil {
    // Map OrderSaveDto to Order
    public static Order fromSaveDto(OrderSaveDto dto){
        Order order = new Order();

        order.setOrderId(null);
        order.setProductId(dto.getProductId());
        order.setCustomerId(dto.getCustomerId());
        order.setQuantity(dto.getQuantity());
        // total price must be set by fetching the product price from db, based on the quantity
        return order;
    }
}
