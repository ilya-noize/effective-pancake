package com.example.user;

import org.springframework.stereotype.Component;

@Component
public class UserDtoConverter {
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
}
