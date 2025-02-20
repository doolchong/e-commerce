package com.example.userservice.service;

import com.example.userservice.dto.UserRequest;
import com.example.userservice.dto.UserResponse;
import org.springframework.data.domain.Page;

public interface UserService {

    UserResponse.Get createUser(UserRequest.Signup userRequest);

    UserResponse.Get getUserByUserId(String userId);

    Page<UserResponse.Summary> getUserByAll(int page, int size);
}
