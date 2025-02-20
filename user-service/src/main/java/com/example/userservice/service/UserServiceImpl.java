package com.example.userservice.service;

import com.example.userservice.dto.UserRequest;
import com.example.userservice.dto.UserResponse;
import com.example.userservice.entity.User;
import com.example.userservice.repository.UserRepository;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponse.Get createUser(UserRequest.Signup userRequest) {
        String encodedPassword = passwordEncoder.encode(userRequest.password());

        User newUser = User.of(encodedPassword, userRequest);
        User savedUser = userRepository.save(newUser);

        return new UserResponse.Get(savedUser);
    }

    @Override
    public UserResponse.Get getUserByUserId(String userId) {
        User user = userRepository.findByUserId(userId).orElseThrow(
                () -> new NotFoundException("User not found"));

        return new UserResponse.Get(user);
    }

    @Override
    public Page<UserResponse.Summary> getUserByAll(int page, int size) {
        return userRepository.findAll(PageRequest.of(page - 1, size)).map(UserResponse.Summary::new);
    }
}
