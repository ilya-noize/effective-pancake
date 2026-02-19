package com.example.order.dto;

import com.example.order.item.OrderItemDto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for the {@link com.example.order.Order} entity.
 * <p/>
 * @param id
 * @param userId
 * @param items
 * @param createdAt
 * @param updatedAt
 * @param status
 */
public record OrderDto(
        Long id,
        Long userId,
        List<OrderItemDto> items,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        String status
) {
}
