package com.example.userservice.service;

import com.example.userservice.dto.UserRequest;
import com.example.userservice.dto.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    UserResponse.Get createUser(UserRequest.Signup userRequest);

    UserResponse.Get getUserByUserId(String userId);

    Page<UserResponse.Summary> getUserByAll(int page, int size);
}
