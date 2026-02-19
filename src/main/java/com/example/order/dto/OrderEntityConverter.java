package com.example.order.dto;

import com.example.order.Order;
import com.example.order.item.OrderItemEntityConverter;
import com.example.user.User;
import org.springframework.stereotype.Component;

@Component
public class OrderEntityConverter {
    private final OrderItemEntityConverter orderItemEntityConverter;

    public OrderEntityConverter(OrderItemEntityConverter orderItemEntityConverter) {
        this.orderItemEntityConverter = orderItemEntityConverter;
    }

    public Order toEntity(OrderDto dto) {
        Order order = new Order();
        order.setItems(dto.items().stream()
                .map(orderItemEntityConverter::toEntity)
                .toList()
        );
        User user = new User(dto.userId(), null, null);
        order.setUser(user);
        order.setUpdatedAt(dto.updatedAt());
        order.setCreatedAt(dto.createdAt());
        order.setStatus(dto.status());

        return order;
    }

    public OrderDto toDomain(Order entity) {

        return new OrderDto(
                entity.getId(),
                entity.getUser().getId(),
                entity.getItems().stream().map(orderItemEntityConverter::toDomain).toList(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getStatus()
        );
    }
}
