package com.example.userservice.dto;

import com.example.userservice.dto.OrderResponse.Get;
import com.example.userservice.dto.OrderResponse.Paged;
import java.time.LocalDateTime;
import java.util.List;

public sealed interface OrderResponse permits Get, Paged {

    record Get(
            String productId,
            String orderId,
            Integer quantity,
            Integer unitPrice,
            Integer totalPrice,
            LocalDateTime createdAt
    ) implements OrderResponse {

    }

    record Paged(
            List<OrderResponse.Get> content,
            Integer totalPages,
            Integer totalElements,
            Integer number,
            Integer size
    ) implements OrderResponse {

    }
}