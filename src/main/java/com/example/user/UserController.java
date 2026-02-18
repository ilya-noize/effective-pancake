package com.example.user;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private static final Logger log = LogManager.getLogger(UserController.class);
    private final UserService userService;
    private final UserDtoConverter dtoConverter;

    public UserController(UserService userService, UserDtoConverter dtoConverter) {
        this.userService = userService;
        this.dtoConverter = dtoConverter;
    }

    @PostMapping
    private UserResponse create(
            @RequestBody
            @Valid UserRequestCreate requestCreate) {
        log.debug("create user");
        UserDto dto = dtoConverter.toEntity(requestCreate);
        UserDto savedUser = userService.createUser(dto);

        return dtoConverter.toDomain(savedUser);
    }

    @PutMapping("{id}")
    private UserResponse update(
            @PathVariable Long id,
            @RequestBody
            @Valid UserRequestUpdate requestUpdate) {
        log.debug("update user");
        UserDto dto = dtoConverter.toEntity(requestUpdate);
        UserDto updateUser = userService.updateUser(id, dto);

        return dtoConverter.toDomain(updateUser);
    }

    @GetMapping("{id}")
    public UserResponse get(
            @PathVariable
            @Positive Long id
    ) {
        UserDto byId = userService.getUserById(id);

        return dtoConverter.toDomain(byId);
    }

    @GetMapping
    public List<UserResponse> getAll(
            @Valid UserSearchFilter filter
    ) {
        log.debug("find all users by filter: [{}]",filter.toString());

        List<UserDto> users = userService.searchUsers(filter);

        return users.stream()
                .map(dtoConverter::toDomain)
                .toList();
    }

    @DeleteMapping("{id}")
    public void delete(
            @PathVariable
            @Positive Long id) {
        log.debug("delete user ID: {}", id);
        userService.deleteUserById(id);
    }
}
