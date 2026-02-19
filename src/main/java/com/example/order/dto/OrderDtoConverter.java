package com.example.order.dto;

import com.example.order.item.OrderItemDtoConverter;
import org.springframework.stereotype.Component;

@Component
public class OrderDtoConverter {

    private final OrderItemDtoConverter itemDtoConverter;

    public OrderDtoConverter(OrderItemDtoConverter itemDtoConverter) {
        this.itemDtoConverter = itemDtoConverter;
    }

    public OrderDto toEntity(OrderPatchRequest request) {
        return new OrderDto(
                request.id(),
                request.userId(),
                request.items().stream().map(itemDtoConverter::toEntity).toList(),
                null, null,
                request.status()
        );
    }

    public OrderResponse toDomain(OrderDto dto) {
        return new OrderResponse(
                dto.id(),
                dto.userId(),
                dto.items().stream()
                        .map(itemDtoConverter::toDomain)
                        .toList(),
                dto.createdAt(),
                dto.updatedAt(),
                dto.status()
        );
    }

    public OrderUserResponse toUserDomain(OrderDto dto) {
        return new OrderUserResponse(
                dto.id(),
                dto.items().stream()
                        .map(itemDtoConverter::toDomain)
                        .toList(),
                dto.createdAt(),
                dto.updatedAt(),
                dto.status()
        );
    }
}
