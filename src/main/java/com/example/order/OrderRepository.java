package com.example.order;

import com.example.product.Product;
import com.example.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {
    // TODO: запросы:

    // Есть ли заказы с указанным пользователем
    boolean existsByUser_Id(Long id);

    // Есть ли заказ у пользователя с таким номером
    boolean existsByUser_IdAndId(Long userId, Long id);

    Optional<Order> findByUser_IdAndId(Long userId, Long id);

    List<Order> findAllByUser_IdAndIdIn(Long id, Collection<Long> ids);


    // 1. Найти все заказы пользователя за последний месяц
    List<Order> findAllByUser_IdAndUpdatedAtBetween(Long id, LocalDateTime updatedAtStart, LocalDateTime updatedAtEnd);

    @Query("""
            SELECT o FROM Order o
            WHERE o.user.id = :userId
            AND o.orderDate >= :date - 30
            """)
    List<Order> findAllUserOrdersInLastMonth(
            @Param("userId") Long userId,
            @Param("date") LocalDate date
    );

    // 2. Найти топ-5 самых продаваемых товаров
//    @Query("""
//            SELECT p FROM Product p
//            ORDER BY p.salesCount DESC LIMIT :limit
//            """)
//    List<Product> findTopSaleProducts(@Param("limit") int limit);

    // 3. Найти общую выручку по категориям товаров
    @Query("""
            SELECT p.category, SUM(oi.cost) AS total
            FROM OrderItem oi
            JOIN oi.product p
            WHERE oi.order.status IN ('COMPLETED', 'DELIVERED')
                 AND oi.order.updatedAt BETWEEN :start AND :end
            GROUP BY p.category
            ORDER BY total DESC
            """)
    List<Object[]> findTotalRevenueByCategory(
            @Param("start") LocalDate start,
            @Param("end") LocalDate end
    );

    // 4. Найти пользователей, которые купили товары из определенной категории
//
//    @Query("""
//            SELECT DISTINCT u
//            FROM User u
//            JOIN u.orders o
//            JOIN o.orderItems oi
//            JOIN oi.product p
//            WHERE p.category = :categoryName
//            """)
//    List<User> findUsersByPurchasedCategory(@Param("categoryName") String categoryName);

    // 5. Обновить статус заказов старше 30 дней на "ARCHIVED"

    @Modifying
    @Query("""
            UPDATE Order o
            SET o.status = 'ARCHIVED'
            WHERE o.orderDate < CURRENT_DATE - 30
            AND o.status <> 'ARCHIVED'
            """)
    int updateStatusToArchivedForOldOrders();

    // 6. Найти средний чек по месяцам

    @Query("""
            SELECT
                FUNCTION('YEAR', o.orderDate) as year,
                FUNCTION('MONTH', o.orderDate) as month,
                AVG(o.totalAmount)
            FROM Order o
            GROUP BY year, month
            ORDER BY year DESC, month DESC
            """)
    List<Object[]> findAverageCheckByMonth();

    // 7. Найти товары, которых осталось меньше 10 на складе

    @Query("""
            SELECT p
            FROM Product p
            WHERE p.stockQuantity < 10
            """)
    List<Product> findProductsLowStock();

}