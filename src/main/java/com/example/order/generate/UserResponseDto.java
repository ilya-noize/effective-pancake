package com.example.order.generate;

import jakarta.validation.constraints.Size;

/**
 * DTO for {@link com.example.user.User}
 */
public record UserResponseDto(
        Long id,
        @Size(min = 3, max = 32) String name,
        @Size(min = 5, max = 255) String email
) {
}