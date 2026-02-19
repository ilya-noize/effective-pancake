package com.example.order;

import com.example.order.dto.OrderDto;
import com.example.order.dto.OrderEntityConverter;
import com.example.order.filter.OrderFilterUpdateBeforeAndStatusIs;
import com.example.order.generate.OrderMapper;
import com.example.order.generate.OrderResponseDto;
import com.example.user.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.Year;
import java.util.Collection;
import java.util.List;

import static com.example.order.generate.OrderMapper.toEntity;
import static com.example.order.generate.OrderMapper.toOrderResponseDto;
import static com.example.order.generate.OrderMapper.updateWithNull;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ObjectMapper objectMapper;
    private final OrderEntityConverter entityConverter;
    private final UserService userService;

    public OrderService(
            OrderRepository orderRepository,
            ObjectMapper objectMapper,
            OrderEntityConverter entityConverter,
            UserService userService
    ) {
        this.orderRepository = orderRepository;
        this.objectMapper = objectMapper;
        this.entityConverter = entityConverter;
        this.userService = userService;
    }

    public Page<OrderResponseDto> getAll(OrderFilterUpdateBeforeAndStatusIs filter, Pageable pageable) {

        return orderRepository.findAll(filter.toSpecification(), pageable)
                .map(OrderMapper::toOrderResponseDto);
    }

    public OrderResponseDto getOne(Long userId, Long id) {
        userService.existsById(userId);
        Order order = orderRepository.findByUser_IdAndId(userId, id).orElseThrow(
                () -> new EntityNotFoundException("No such order with this user's ID:" + userId)
        );
        return toOrderResponseDto(order);
    }

    public List<OrderResponseDto> getMany(Long userId, List<Long> ids) {
        List<Order> orders = orderRepository.findAllByUser_IdAndIdIn(userId, ids);
        if (orders.isEmpty()) {
            return List.of();
        }
        return orders.stream()
                .map(OrderMapper::toOrderResponseDto)
                .toList();
    }

    public OrderResponseDto create(Long userId, OrderResponseDto dto) {
        validateUserIdAndOrderUserId(userId, dto.user().id());
        Order order = toEntity(dto);
        Order resultOrder = orderRepository.save(order);

        return toOrderResponseDto(resultOrder);
    }

    public OrderDto patch(Long userId, Long id, OrderDto dto) {
        validateUserIdAndOrderUserIds(userId, List.of(dto.userId(), id));
        Order entity = entityConverter.toEntity(dto);

        return entityConverter.toDomain(orderRepository.save(entity));

//        Order order = orderRepository.findById(id).orElseThrow(() ->
//                new ResponseStatusException(HttpStatus.NOT_FOUND, "Order with id `%s` not found".formatted(id)));
//
//        OrderResponseDto orderResponseDto = toOrderResponseDto(order);
//        objectMapper.readerForUpdating(orderResponseDto).readValue(dto);
//        updateWithNull(orderResponseDto, order);
//        Order resultOrder = orderRepository.save(entity);
//        return toOrderResponseDto(resultOrder);
    }

    public List<Long> patchMany(Long userId, List<Long> ids, JsonNode patchNode) {
        Collection<Order> orders = orderRepository.findAllById(ids);

        for (Order order : orders) {
            OrderResponseDto orderResponseDto = toOrderResponseDto(order);
            objectMapper.readerForUpdating(orderResponseDto).readValue(patchNode);
            updateWithNull(orderResponseDto, order);
        }

        List<Order> resultOrders = orderRepository.saveAll(orders);
        return resultOrders.stream()
                .map(Order::getId)
                .toList();
    }

    public OrderResponseDto delete(Long userId, Long id) {

        Order order = orderRepository.findById(id).orElse(null);
        if (order != null) {
            orderRepository.delete(order);
        }
        return toOrderResponseDto(order);
    }

    public void deleteMany(Long userId, List<Long> ids) {
        validateUserIdAndOrderUserIds(userId, ids);
        orderRepository.deleteAllById(ids);
    }

    public int archiveOldOrders() {
        return orderRepository.updateStatusToArchivedForOldOrders();
    }

    /**
     * История заказов пользователя за период
     * @param userId
     * @param afterDate
     * @param beforeDate
     * @return
     */
    public List<OrderDto> getUserOrderHistory(Long userId, LocalDateTime afterDate, LocalDateTime beforeDate) {
        List<Order> orders = orderRepository.findAllByUser_IdAndUpdatedAtBetween(userId, afterDate, beforeDate);
        if (orders.isEmpty()) return List.of();

        return orders
                .stream()
                .sorted((d1, d2) -> d2.getUpdatedAt().compareTo(d1.getUpdatedAt()))
                .map(entityConverter::toDomain)
                .toList();
    }

    /**
     * Выручка по категориям в разрезе месяца
     * @param categoryName
     * @param revenue
     */
    public record RevenueByCategoryMonthlyReport(
            String categoryName,
            BigDecimal revenue
    ) {
    }

    /**
     * Выручка по категориям за месяц
     * @param monthNumber
     * @return
     */
    public List<RevenueByCategoryMonthlyReport> getMonthlyRevenue(int monthNumber) {
        if (monthNumber < 0 || monthNumber > 12) {
            throw new IllegalArgumentException("Invalid number of the MONTH: %s"
                    .formatted(monthNumber));
        }
        if (LocalDate.now().getMonth().getValue()<monthNumber) {
            throw new IllegalArgumentException("MONTH:%s in future".formatted(monthNumber));
        }

        LocalDate start = LocalDate.of(
                Year.now().getValue(),
                Month.of(monthNumber).getValue(),
                1);
        LocalDate end = start.plusMonths(1).minusDays(1);
        return orderRepository
                .findTotalRevenueByCategory(start, end).stream()
                .map(row -> new RevenueByCategoryMonthlyReport(
                        (String) row[0],
                        (BigDecimal) row[1]
                ))
                .toList();
    }

    private void validateUserIdAndOrderUserId(Long userId, Long orderUserId) {
        userService.existsById(userId);
        userService.existsByIdAndOrderId(userId, orderUserId);
        if (!userId.equals(orderUserId)) {
            throw new IllegalArgumentException("Ошибка в создании заказа (userID не совпадает с userID в заказе)");
        }
    }

    private void validateUserIdAndOrderUserIds(Long userId, List<Long> orderUserIds) {
        userService.existsById(userId);
        userService.existsByIdAndOrderIds(userId, orderUserIds);
        orderUserIds.forEach(orderUserId -> {
            if (!orderUserIds.contains(userId)) {
                throw new IllegalArgumentException("Ошибка в создании заказа (userID не совпадает с userID в заказе)");
            }
        });

    }
}
