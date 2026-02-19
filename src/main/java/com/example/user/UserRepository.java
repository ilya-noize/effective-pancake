package com.example.user;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);

    boolean existsByIdAndOrders_Id(Long userId, Long orderId);

    boolean existsByIdAndOrders_IdIn(Long userId, List<Long> orderIds);

    List<User> findAllByNameContainsIgnoreCaseAndEmailContainsIgnoreCaseOrderById(
            String name,
            String mail,
            Pageable page,
            Sort sort
    );
}
