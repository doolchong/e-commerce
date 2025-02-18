package com.example.orderservice.controller;

import com.example.orderservice.dto.OrderRequest;
import com.example.orderservice.dto.OrderResponse;
import com.example.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/users/{userId}")
    public ResponseEntity<OrderResponse.Get> createOrder(
            @PathVariable("userId") String userId,
            @RequestBody OrderRequest.Create orderRequest
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(orderService.createOrder(userId, orderRequest));
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<Page<OrderResponse.List>> getOrderList(
            @PathVariable("userId") String userId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(orderService.getOrderListByUserId(userId, page, size));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse.Get> getOrder(@PathVariable("orderId") String orderId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(orderService.getOrderByOrderId(orderId));
    }
}
