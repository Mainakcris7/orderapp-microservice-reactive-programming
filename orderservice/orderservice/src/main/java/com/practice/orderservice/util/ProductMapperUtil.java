package com.practice.orderservice.util;

import com.practice.orderservice.dto.ProductSaveDto;
import com.practice.orderservice.dto.ProductUpdateDto;
import com.practice.orderservice.model.Product;

public class ProductMapperUtil {
    // Map ProductSaveDto to Product
    public static Product fromUpdateDto(ProductUpdateDto dto){
        Product product = new Product();

        product.setProductId(dto.getProductId());
        product.setProductName(dto.getProductName());
        product.setStockQuantity(dto.getQuantity());
        product.setPrice(dto.getPrice());

        return product;
    }

    // Map ProductUpdateDto to Product
    public static Product fromSaveDto(ProductSaveDto dto){
        Product product = new Product();

        product.setProductId(null);
        product.setProductName(dto.getProductName());
        product.setStockQuantity(dto.getQuantity());
        product.setPrice(dto.getPrice());

        return product;
    }
}
