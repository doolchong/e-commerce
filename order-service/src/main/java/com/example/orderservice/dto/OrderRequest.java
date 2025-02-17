package com.example.orderservice.dto;

public sealed interface OrderRequest permits OrderRequest.Create {
    record Create(
            String productName,
            Integer stock,
            Integer unitPrice) implements OrderRequest {
    }
}

