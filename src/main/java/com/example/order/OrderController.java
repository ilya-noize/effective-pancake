package com.example.order;

import com.example.order.dto.OrderDto;
import com.example.order.dto.OrderDtoConverter;
import com.example.order.dto.OrderPatchRequest;
import com.example.order.dto.OrderResponse;
import com.example.order.filter.OrderFilterUpdateBeforeAndStatusIs;
import com.example.order.generate.OrderResponseDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import tools.jackson.databind.JsonNode;

import java.util.List;

@RestController
@RequestMapping("/users/{userId}/orders")
public class OrderController {

    private final OrderService orderService;
    private final OrderDtoConverter dtoConverter;

    public OrderController(
            OrderService orderService,
            OrderDtoConverter dtoConverter
    ) {
        this.orderService = orderService;
        this.dtoConverter = dtoConverter;
    }

    @GetMapping
    public PagedModel<OrderResponseDto> getAll(
            @ModelAttribute OrderFilterUpdateBeforeAndStatusIs filter, Pageable pageable
    ) {
        Page<OrderResponseDto> orderResponseDtos = orderService.getAll(filter, pageable);
        return new PagedModel<>(orderResponseDtos);
    }

    @GetMapping("/{id}")
    public OrderResponseDto getOne(
            @PathVariable("userId")
            @Positive Long userId,
            @PathVariable
            @Positive Long id
    ) {
        return orderService.getOne(userId, id);
    }

    @GetMapping("/by-ids")
    public List<OrderResponseDto> getMany(
            @PathVariable("userId")
            @Positive Long userId,
            @RequestParam List<Long> ids
    ) {
        return orderService.getMany(userId, ids);
    }

    @PostMapping
    public OrderResponseDto create(
            @PathVariable("userId")
            @Positive Long userId,
            @RequestBody
            @Valid OrderResponseDto dto
    ) {
        return orderService.create(userId, dto);
    }

    @PatchMapping("/{id}")
    public OrderResponse patch(
            @PathVariable("userId") Long userId,
            @PathVariable Long id,
            @RequestBody
            @Valid OrderPatchRequest patchNode
    ) {
        OrderDto dto = dtoConverter.toEntity(patchNode);
        OrderDto patch = orderService.patch(userId, id, dto);
        return dtoConverter.toDomain(patch);
    }

    @PatchMapping
    public List<Long> patchMany(
            @PathVariable("userId")
            @Positive Long userId,
            @RequestParam List<Long> ids,
            @RequestBody JsonNode patchNode
    ) {
        return orderService.patchMany(userId, ids, patchNode);
    }

    @DeleteMapping("/{id}")
    public OrderResponseDto delete(
            @PathVariable("userId")
            @Positive Long userId,
            @PathVariable
            @Positive Long id
    ) {
        return orderService.delete(userId, id);
    }

    @DeleteMapping
    public void deleteMany(
            @PathVariable("userId")
            @Positive Long userId,
            @RequestParam List<Long> ids
    ) {
        orderService.deleteMany(userId, ids);
    }
}
