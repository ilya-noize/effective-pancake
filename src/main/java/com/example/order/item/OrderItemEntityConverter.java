package com.example.order.item;

import com.example.product.ProductEntityConverter;
import org.springframework.stereotype.Component;

@Component
public class OrderItemEntityConverter {

    private final ProductEntityConverter productEntityConverter;

    public OrderItemEntityConverter(ProductEntityConverter productEntityConverter) {
        this.productEntityConverter = productEntityConverter;
    }

    public OrderItem toEntity(OrderItemDto dto) {

        return new OrderItem(
                dto.id(),
                null,
                productEntityConverter.toEntity(dto.product()),
                dto.quantity(),
                dto.cost()
        );
    }

    public OrderItemDto toDomain(OrderItem entity) {
        return new OrderItemDto(
                entity.getId(),
                productEntityConverter.toDomain(entity.getProduct()),
                entity.getQuantity(),
                entity.getCost()

        );
    }
}
