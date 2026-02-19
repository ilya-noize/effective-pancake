package com.example.order.item;

import com.example.product.ProductDtoConverter;
import org.springframework.stereotype.Component;

@Component
public class OrderItemDtoConverter {

    private final ProductDtoConverter productDtoConverter;

    public OrderItemDtoConverter(ProductDtoConverter productDtoConverter) {
        this.productDtoConverter = productDtoConverter;
    }

    public OrderItemResponse toDomain(OrderItemDto dto) {
        return new OrderItemResponse(
                dto.id(),
                productDtoConverter.toDomain(dto.product()),
                dto.quantity(),
                dto.cost()
        );
    }

    public OrderItemDto toEntity(OrderItemPatchRequest dto) {
        return new OrderItemDto(
                dto.id(),
                productDtoConverter.toEntity(dto.product()),
                dto.quantity(),
                dto.cost()
        );
    }
}
