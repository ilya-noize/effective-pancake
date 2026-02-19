package com.example.order.item;

import com.example.product.ProductResponse;

import java.math.BigDecimal;


public record OrderItemResponse(
        Long id,
        ProductResponse product,
        Integer quantity,
        BigDecimal cost
) {
}
