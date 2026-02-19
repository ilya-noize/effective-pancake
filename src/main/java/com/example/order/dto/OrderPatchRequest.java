package com.example.order.dto;

import com.example.order.item.OrderItemPatchRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

import java.util.List;

public record OrderPatchRequest(
        @Positive
        Long id,

        @Positive
        Long userId,

        List<OrderItemPatchRequest> items,

        @NotBlank
        String status
) {
}
