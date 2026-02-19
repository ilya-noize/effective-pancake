package com.example.order.generate;

import com.example.order.item.OrderItem;

import java.math.BigDecimal;

/**
 * DTO for {@link OrderItem}
 */
public record OrderItemResponseDto(Long id, ProductResponseDto product, Integer quantity, BigDecimal cost) {
}