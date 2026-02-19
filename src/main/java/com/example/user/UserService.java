package com.example.user;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserEntityConverter entityConverter;

    public UserService(UserRepository userRepository, UserEntityConverter entityConverter) {
        this.userRepository = userRepository;
        this.entityConverter = entityConverter;
    }

    public UserDto createUser(UserDto dto) {
        if (userRepository.existsByEmail(dto.email())) {
            throw new IllegalArgumentException("Email already exists");
        }
        User save = userRepository.save(entityConverter.toEntity(dto));

        return entityConverter.toDomain(save);
    }

    public UserDto updateUser(Long id, UserDto dto) {
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("no such user ID:" + id);
        }
        User update = userRepository.save(entityConverter.toEntity(dto));

        return entityConverter.toDomain(update);
    }

    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("no such user ID:" + id)
        );

        return entityConverter.toDomain(user);
    }

    public List<UserDto> searchUsers(UserSearchFilter filter) {
        Pageable pageable = Pageable.ofSize(filter.pageSize())
                .withPage(filter.pageNumber());
        Sort sort = Sort.by(
                Sort.Direction.fromString(filter.ascending() ? "asc" : "desc"),
                filter.sortBy()
        );
        List<User> findUsers = userRepository.findAllByNameContainsIgnoreCaseAndEmailContainsIgnoreCaseOrderById(
                filter.name(),
                filter.email(),
                pageable,
                sort
        );

        return findUsers.stream().map(entityConverter::toDomain).toList();
    }

    public void deleteUserById(Long id) {
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("no such user ID:" + id);
        }
        userRepository.deleteById(id);
    }

    public void existsById(Long id) {
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("No such user by ID:" + id);
        }
    }

    public void existsByIdAndOrderId(Long userId, Long orderId) {
        if (!userRepository.existsByIdAndOrders_Id(userId, orderId)) {
            throw new EntityNotFoundException("No such order ID:%s by user by ID:%s"
                    .formatted(userId, orderId)
            );
        }
    }

    public void existsByIdAndOrderIds(Long userId, List<Long> orderIds) {
        if (!userRepository.existsByIdAndOrders_IdIn(userId, orderIds)) {
            throw new EntityNotFoundException("No such orders ID:%s by user by ID:%s"
                    .formatted(userId, orderIds)
            );
        }
    }
}
