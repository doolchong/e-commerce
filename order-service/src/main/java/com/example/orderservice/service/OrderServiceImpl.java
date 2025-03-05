package com.example.orderservice.service;

import com.example.orderservice.dto.OrderRequest;
import com.example.orderservice.dto.OrderResponse;
import com.example.orderservice.entity.Order;
import com.example.orderservice.repository.OrderRepository;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    @Override
    @Transactional
    public OrderResponse.Get createOrder(String userId, OrderRequest.Create orderRequest) {
        Order newOrder = Order.of(userId, orderRequest);
        Order savedOrder = orderRepository.save(newOrder);

        return new OrderResponse.Get(savedOrder);
    }

    @Override
    public OrderResponse.Get getOrderByOrderId(String orderId) {
        Order order = orderRepository.findByOrderId(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found"));

        return new OrderResponse.Get(order);
    }

    @Override
    public Page<OrderResponse.Get> getOrderListByUserId(String userId, int page, int size) {
        return orderRepository.findByUserId(userId, PageRequest.of(page - 1, size))
                .map(OrderResponse.Get::new);
    }
}
