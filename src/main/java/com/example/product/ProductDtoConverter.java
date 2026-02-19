package com.example.product;

import org.springframework.stereotype.Component;

@Component
public class ProductDtoConverter {

    public ProductDto toEntity(ProductPostRequest post) {

        return new ProductDto(
                null,
                post.name(),
                post.category(),
                post.price(),
                post.stockQuantity()
        );
    }

    public ProductDto toEntity(ProductPatchRequest patch) {

        return new ProductDto(
                patch.id(),
                patch.name(),
                patch.category(),
                patch.price(),
                patch.stockQuantity()
        );
    }

    public ProductResponse toDomain(ProductDto dto) {

        return new ProductResponse(
                dto.id(),
                dto.name(),
                dto.category(),
                dto.price(),
                dto.stockQuantity()
        );
    }
}
