package com.example.order.filter;

import com.example.order.Order;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

public record OrderFilterUpdateBeforeAndStatusIs(Long userId, LocalDateTime updatedAtLte, String status) {
    public Specification<Order> toSpecification() {
        return updatedAtLteSpec()
                .and(statusSpec())
                .and(userSpec());
    }

    private Specification<Order> updatedAtLteSpec() {
        return ((root, query, cb) -> updatedAtLte != null
                ? cb.lessThanOrEqualTo(root.get("updatedAt"), updatedAtLte)
                : null);
    }

    private Specification<Order> statusSpec() {
        return ((root, query, cb) -> StringUtils.hasText(status)
                ? cb.equal(cb.lower(root.get("status")), status.toLowerCase())
                : null);
    }

    private Specification<Order> userSpec() {
        return ((root, query, cb) -> userId != null
                ? cb.equal(cb.lower(root.get("user.id")), userId)
                : null);
    }
}