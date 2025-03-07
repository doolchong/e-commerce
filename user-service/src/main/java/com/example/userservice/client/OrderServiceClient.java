package com.example.userservice.client;

import com.example.userservice.dto.OrderResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "order-service")
public interface OrderServiceClient {

    @GetMapping("/orders/users/{userId}")
    OrderResponse.Paged getOrderList(@PathVariable("userId") String userId);
}
