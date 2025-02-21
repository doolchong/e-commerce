package com.example.catalogservice.dto;

import com.example.catalogservice.dto.CatalogResponse.List;
import com.example.catalogservice.entity.Catalog;
import java.time.LocalDateTime;

public sealed interface CatalogResponse permits List {

    record List(
            String productId,
            String productName,
            Integer stock,
            Integer unitPrice,
            LocalDateTime createdAt
    ) implements CatalogResponse {

        public List(Catalog catalog) {
            this(
                    catalog.getProductId(),
                    catalog.getProductName(),
                    catalog.getStock(),
                    catalog.getUnitPrice(),
                    catalog.getCreatedAt());
        }
    }
}