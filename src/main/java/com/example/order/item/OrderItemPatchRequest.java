package com.example.order.item;

import com.example.product.ProductPatchRequest;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record OrderItemPatchRequest(
        @Positive
        Long id,

        @Positive
        Long orderId,

        @NotNull
        ProductPatchRequest product,

        @Positive
        Integer quantity,

        @Positive
        BigDecimal cost
) {
}
