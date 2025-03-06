package com.example.userservice.dto;

import com.example.userservice.dto.OrderResponse.Get;
import com.example.userservice.dto.OrderResponse.Paged;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;
import java.util.List;

public sealed interface OrderResponse permits Get, Paged {


    @JsonInclude(JsonInclude.Include.NON_NULL)
    record Get(
            String productId,
            String orderId,
            Integer quantity,
            Integer unitPrice,
            Integer totalPrice,
            LocalDateTime createdAt
    ) implements OrderResponse {

    }


    @JsonInclude(JsonInclude.Include.NON_NULL)
    record Paged(
            List<OrderResponse.Get> content,
            Integer totalPages,
            Integer totalElements,
            Integer number,
            Integer size
    ) implements OrderResponse {

    }
}