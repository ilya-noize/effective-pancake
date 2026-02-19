package com.example.report;

import com.example.order.OrderService;
import com.example.order.dto.OrderDto;
import com.example.product.ProductDto;
import com.example.product.ProductService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class ReportService {

    private final ProductService productService;
    private final OrderService orderService;

    public ReportService(
            ProductService productService,
            OrderService orderService
    ) {
        this.productService = productService;
        this.orderService = orderService;
    }

    // выручка за месяц
    public List<OrderService.RevenueByCategoryMonthlyReport> getMonthlyRevenue(int month) {

        return orderService.getMonthlyRevenue(month);
    }

    // самые продаваемые товары
    public List<ProductDto> topSoldProduct(Integer topLimit) {

        return productService.getTopSellingProducts(topLimit);
    }

    // история заказов пользователя
    public List<OrderDto> getUserOrderHistory(
            Long userId,
            LocalDateTime afterDate,
            LocalDateTime beforeDate
    ) {
        return orderService.getUserOrderHistory(userId, afterDate, beforeDate);
    }

    // архивация старых заказов
    public int archiveOldOrders() {
        int archiveOldOrders = orderService.archiveOldOrders();
        String message = "Архивировано " + archiveOldOrders + " записей";
        System.out.println(message);

        return archiveOldOrders;
    }

    // товары с низким запасом
    public List<ProductDto> getLowStockProducts(Integer minStockQuantity) {

        return productService.getLowStockProducts(minStockQuantity);
    }
}
