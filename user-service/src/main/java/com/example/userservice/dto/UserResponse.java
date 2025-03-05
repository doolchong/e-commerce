package com.example.userservice.dto;

import com.example.userservice.dto.UserResponse.Get;
import com.example.userservice.dto.UserResponse.Summary;
import com.example.userservice.entity.User;

public sealed interface UserResponse permits Get, Summary {

    record Get(
            String email,
            String name,
            String userId,
            OrderResponse.Paged orderList
    ) implements UserResponse {

        public Get(User user, OrderResponse.Paged orderList) {
            this(
                    user.getEmail(),
                    user.getName(),
                    user.getUserId(),
                    orderList
            );
        }
    }

    record Summary(
            String email,
            String name,
            String userId
    ) implements UserResponse {

        public Summary(User user) {
            this(
                    user.getEmail(),
                    user.getName(),
                    user.getUserId()
            );
        }
    }
}