package com.example.order.generate;

import java.math.BigDecimal;

/**
 * DTO for {@link com.example.product.Product}
 */
public record ProductResponseDto(
        Long id,
        String name,
        String category,
        BigDecimal price,
        Integer stockQuantity
) {
}