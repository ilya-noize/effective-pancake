package com.example.user;

import com.example.order.dto.OrderUserResponse;

import java.util.List;

public record UserOrdersResponse(
        Long id,
        String name,
        String email,
        List<OrderUserResponse> orders
){
}
