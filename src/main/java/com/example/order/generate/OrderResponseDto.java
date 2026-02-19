package com.example.order.generate;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for {@link com.example.order.Order}
 *
 * @param id
 * @param user
 * @param items
 * @param createdAt
 * @param updatedAt
 * @param status
 */
public record OrderResponseDto(
        Long id,
        UserResponseDto user,
        List<OrderItemResponseDto> items,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        String status
) {

}