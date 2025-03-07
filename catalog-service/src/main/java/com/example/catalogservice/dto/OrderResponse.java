package com.example.catalogservice.dto;

import com.example.catalogservice.dto.OrderResponse.Get;
import java.time.LocalDateTime;

public sealed interface OrderResponse permits Get {

    record Get(
            String productId,
            String orderId,
            Integer quantity,
            Integer unitPrice,
            Integer totalPrice,
            LocalDateTime createdAt
    ) implements OrderResponse {

    }
}