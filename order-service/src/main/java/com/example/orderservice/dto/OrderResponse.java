package com.example.orderservice.dto;

import com.example.orderservice.dto.OrderResponse.Get;
import com.example.orderservice.entity.Order;
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

        public Get(Order order) {
            this(
                    order.getProductId(),
                    order.getOrderId(),
                    order.getQuantity(),
                    order.getUnitPrice(),
                    order.getTotalPrice(),
                    order.getCreatedAt()
            );
        }
    }
}