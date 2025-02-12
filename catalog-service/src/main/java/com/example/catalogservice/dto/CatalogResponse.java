package com.example.catalogservice.dto;

import com.example.catalogservice.dto.CatalogResponse.Get;
import com.example.catalogservice.entity.Catalog;

import java.time.LocalDateTime;

public sealed interface CatalogResponse permits Get {

    record Get(
            String productId,
            String productName,
            Integer stock,
            Integer unitPrice,
            LocalDateTime createdAt
    ) implements CatalogResponse {
        public Get(Catalog catalog) {
            this(
                    catalog.getProductId(),
                    catalog.getProductName(),
                    catalog.getStock(),
                    catalog.getUnitPrice(),
                    catalog.getCreatedAt()
            );
        }
    }
}