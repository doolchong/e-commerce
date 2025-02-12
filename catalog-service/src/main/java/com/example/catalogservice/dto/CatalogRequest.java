package com.example.catalogservice.dto;

import com.example.catalogservice.dto.CatalogRequest.Create;

public sealed interface CatalogRequest permits Create {
    record Create(
            String productName,
            Integer stock,
            Integer unitPrice) implements CatalogRequest {
    }
}

