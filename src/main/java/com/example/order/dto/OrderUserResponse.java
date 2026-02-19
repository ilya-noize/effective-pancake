package com.example.order.dto;

import com.example.order.item.OrderItemResponse;

import java.time.LocalDateTime;
import java.util.List;

public record OrderUserResponse(
        Long id,
        List<OrderItemResponse> items,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        String status
) {
}
