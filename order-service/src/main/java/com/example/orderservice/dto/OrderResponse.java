package com.example.orderservice.dto;

import com.example.orderservice.dto.OrderResponse.Field;
import com.example.orderservice.dto.OrderResponse.Get;
import com.example.orderservice.dto.OrderResponse.Kafka;
import com.example.orderservice.dto.OrderResponse.Payload;
import com.example.orderservice.dto.OrderResponse.Schema;
import com.example.orderservice.entity.Order;
import java.time.LocalDateTime;
import java.util.List;

public sealed interface OrderResponse permits Get, Field, Schema, Payload, Kafka {

    record Get(
            String orderId,
            String userId,
            String productId,
            Integer quantity,
            Integer unitPrice,
            Integer totalPrice,
            LocalDateTime createdAt
    ) implements OrderResponse {

        public Get(Order order) {
            this(
                    order.getOrderId(),
                    order.getUserId(),
                    order.getProductId(),
                    order.getQuantity(),
                    order.getUnitPrice(),
                    order.getTotalPrice(),
                    order.getCreatedAt()
            );
        }
    }

    record Field(
            String type,
            Boolean optional,
            String field
    ) implements OrderResponse {

    }

    record Schema(
            String type,
            List<Field> fieldList,
            Boolean optional,
            String name
    ) implements OrderResponse {

    }

    record Payload(
            String orderId,
            String userId,
            String productId,
            Integer quantity,
            Integer unitPrice,
            Integer totalPrice
    ) implements OrderResponse {

    }

    record Kafka(
            Schema schema,
            Payload payload
    ) implements OrderResponse {

    }
}