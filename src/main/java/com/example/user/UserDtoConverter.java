package com.example.user;

import com.example.order.dto.OrderDto;
import com.example.order.dto.OrderDtoConverter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserDtoConverter {

    private final OrderDtoConverter orderDtoConverter;

    public UserDtoConverter(OrderDtoConverter orderDtoConverter) {
        this.orderDtoConverter = orderDtoConverter;
    }

    public UserDto toEntity(UserRequestCreate entity) {

        return new UserDto(
                null,
                entity.name(),
                entity.email()
        );
    }

    public UserDto toEntity(UserRequestUpdate entity) {

        return new UserDto(
                entity.id(),
                entity.name(),
                entity.email()
        );
    }

    public UserResponse toDomain(UserDto dto) {

        return new UserResponse(
                dto.id(),
                dto.name(),
                dto.email()
        );
    }

    public UserOrdersResponse toDomain(UserDto dto, List<OrderDto> orderDtos) {
        return new UserOrdersResponse(
                dto.id(),
                dto.name(),
                dto.email(),
                orderDtos.stream()
                        .map(orderDtoConverter::toUserDomain)
                        .toList()
        );
    }
}
