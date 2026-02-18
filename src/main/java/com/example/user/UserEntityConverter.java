package com.example.user;

import org.springframework.stereotype.Component;

@Component
public class UserEntityConverter {
    public UserDto toDomain(User entity) {

        return new UserDto(
                entity.getId(),
                entity.getName(),
                entity.getEmail()
        );
    }

    public User toEntity(UserDto dto) {

        return new User(
                dto.id(),
                dto.name(),
                dto.email()
        );
    }
}
