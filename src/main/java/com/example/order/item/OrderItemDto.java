package com.example.order.item;

import com.example.product.ProductDto;

import java.math.BigDecimal;


public record OrderItemDto(
        Long id,
        ProductDto product,
        Integer quantity,
        BigDecimal cost
) {
}
