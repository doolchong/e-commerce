package com.example.orderservice.dto;

public sealed interface OrderRequest permits OrderRequest.Create {
    record Create(
            String productId,
            Integer quantity,
            Integer unitPrice) implements OrderRequest {
    }
}

