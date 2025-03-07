package com.example.orderservice.service;

import com.example.orderservice.dto.OrderResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaProducer {

    private final KafkaTemplate<String, OrderResponse.Get> kafkaTemplate;

    public OrderResponse.Get send(String topic, OrderResponse.Get orderResponse) {
        kafkaTemplate.send(topic, orderResponse);

        log.info("Kafka Producer sent data from the Order microservice {}", orderResponse);

        return orderResponse;
    }
}
