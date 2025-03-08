package com.example.orderservice.service;

import com.example.orderservice.dto.OrderResponse;
import com.example.orderservice.dto.OrderResponse.Kafka;
import com.example.orderservice.dto.OrderResponse.Schema;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final List<OrderResponse.Field> fieldList = Arrays.asList(
            new OrderResponse.Field("string", true, "orderId"),
            new OrderResponse.Field("string", true, "userId"),
            new OrderResponse.Field("string", true, "productId"),
            new OrderResponse.Field("int32", true, "quantity"),
            new OrderResponse.Field("int32", true, "unitPrice"),
            new OrderResponse.Field("int32", true, "totalPrice")
    );
    private final OrderResponse.Schema schema = new Schema(
            "struct",
            fieldList,
            false,
            "orders"
    );

    public OrderResponse.Get send(String topic, OrderResponse.Get orderResponse) {
        OrderResponse.Payload payload = new OrderResponse.Payload(
                orderResponse.orderId(),
                orderResponse.userId(),
                orderResponse.productId(),
                orderResponse.quantity(),
                orderResponse.unitPrice(),
                orderResponse.totalPrice()
        );

        OrderResponse.Kafka orderResponseKafka = new Kafka(schema, payload);
        kafkaTemplate.send(topic, orderResponseKafka);

        return orderResponse;
    }
}
