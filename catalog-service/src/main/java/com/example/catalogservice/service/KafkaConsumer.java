package com.example.catalogservice.service;

import com.example.catalogservice.dto.OrderResponse;
import com.example.catalogservice.entity.Catalog;
import com.example.catalogservice.repository.CatalogRepository;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaConsumer {

    private final CatalogRepository catalogRepository;

    @KafkaListener(
            topics = "example-catalog-topic",
            groupId = "consumerGroupId",
            containerFactory = "kafkaListenerContainerFactory"
    )
    @Transactional
    public void updateQuantity(OrderResponse.Get kafkaMessage) {
        log.info("Kafka Message: -> {}", kafkaMessage.toString());

        Catalog catalog = catalogRepository.findByProductId(kafkaMessage.productId())
                .orElseThrow(
                        () -> new NotFoundException("Not found catalog")
                );

        catalog.updateStock(kafkaMessage.quantity());
    }
}
