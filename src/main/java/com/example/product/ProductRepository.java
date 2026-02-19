package com.example.product;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("""
    SELECT p FROM Product p
    JOIN FETCH p.orderItems oi
    GROUP BY p.id, p.name, p.category, p.price, p.stockQuantity
    ORDER BY SUM(io.quantity) DESC)
    """)
    List<Product> getTopSellingProducts(Pageable pageable);

    List<Product> findAllByStockQuantityLessThanEqual(Integer stockQuantity);
}