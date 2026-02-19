package com.example.order.generate;

import com.example.order.Order;
import com.example.order.item.OrderItem;
import com.example.product.Product;
import com.example.user.User;

import java.util.List;

public class OrderMapper {

    public static OrderResponseDto toOrderResponseDto(Order order) {
        User user = order.getUser();
        UserResponseDto userDto = new UserResponseDto(
                user.getId(),
                user.getName(),
                user.getEmail()
        );

        List<OrderItemResponseDto> itemDtos = order.getItems().stream()
                .map(item -> new OrderItemResponseDto(
                        item.getId(),
                        toProductResponseDto(item.getProduct()),
                        item.getQuantity(),
                        item.getCost()
                ))
                .toList();

        return new OrderResponseDto(
                order.getId(),
                userDto,
                itemDtos,
                order.getCreatedAt(),
                order.getUpdatedAt(),
                order.getStatus()
        );
    }

    public static Order toEntity(OrderResponseDto dto) {

        return new Order(
                dto.id(),
                toUserEntity(dto.user()),
                toOrderItems(dto.items()),
                dto.createdAt(),
                dto.updatedAt(),
                dto.status()
        );
    }

    public static Order updateWithNull(OrderResponseDto dto, Order order) {
        order.setUser(toUserEntity(dto.user()));
        order.setItems(toOrderItems(dto.items()));
        order.setStatus(dto.status());

        return order;
    }

    // Вспомогательные методы

    private static ProductResponseDto toProductResponseDto(Product product) {

        return new ProductResponseDto(
                product.getId(),
                product.getName(),
                product.getCategory(),
                product.getPrice(),
                product.getStockQuantity()
        );
    }

    private static User toUserEntity(UserResponseDto userDto) {

        return new User(
                userDto.id(),
                userDto.name(),
                userDto.email()
        );
    }

    private static List<OrderItem> toOrderItems(List<OrderItemResponseDto> itemDtos) {

        return itemDtos.stream()
                .map(orderItemResponseDto -> {

                    var dto = orderItemResponseDto.product();
                    var product =new Product(
                            dto.id(),
                            dto.name(),
                            dto.category(),
                            dto.price(),
                            dto.stockQuantity()
                    );

                    return new OrderItem(
                            orderItemResponseDto.id(),
                            null,
                            product,
                            orderItemResponseDto.quantity(),
                            orderItemResponseDto.cost()
                    );
                })
                .toList();
    }
}