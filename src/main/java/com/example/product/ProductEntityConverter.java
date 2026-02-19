package com.example.product;

import org.springframework.stereotype.Component;

@Component
public class ProductEntityConverter {

    public Product toEntity(ProductDto dto) {

        return new Product(
                dto.id(),
                dto.name(),
                dto.category(),
                dto.price(),
                dto.stockQuantity()
        );
    }


    public ProductDto toDomain(Product entity) {

        return new ProductDto(
                entity.getId(),
                entity.getName(),
                entity.getCategory(),
                entity.getPrice(),
                entity.getStockQuantity()
        );
    }
}
