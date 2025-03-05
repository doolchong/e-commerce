package com.example.orderservice.service;

import com.example.orderservice.dto.OrderRequest;
import com.example.orderservice.dto.OrderResponse;
import org.springframework.data.domain.Page;

public interface OrderService {

    OrderResponse.Get createOrder(String userId, OrderRequest.Create orderRequest);

    OrderResponse.Get getOrderByOrderId(String orderId);

    Page<OrderResponse.Get> getOrderListByUserId(String userId, int page, int size);
}
