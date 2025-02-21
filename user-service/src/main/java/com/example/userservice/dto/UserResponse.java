package com.example.userservice.dto;

import com.example.userservice.dto.UserResponse.Get;
import com.example.userservice.dto.UserResponse.Summary;
import com.example.userservice.entity.User;
import java.util.ArrayList;

public sealed interface UserResponse permits Get, Summary {

    record Get(
            String email,
            String name,
            String userId,
            java.util.List<ResponseOrder> orderList
    ) implements UserResponse {

        public Get(User user) {
            this(
                    user.getEmail(),
                    user.getName(),
                    user.getUserId(),
                    new ArrayList<>()
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