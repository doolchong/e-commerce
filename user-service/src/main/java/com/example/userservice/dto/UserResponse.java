package com.example.userservice.dto;

import com.example.userservice.dto.UserResponse.Get;
import com.example.userservice.dto.UserResponse.Summary;
import com.example.userservice.entity.User;
import com.fasterxml.jackson.annotation.JsonInclude;

public sealed interface UserResponse permits Get, Summary {


    @JsonInclude(JsonInclude.Include.NON_NULL)
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


    @JsonInclude(JsonInclude.Include.NON_NULL)
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