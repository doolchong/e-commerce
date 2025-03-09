package com.example.userservice.service;

import com.example.userservice.client.OrderServiceClient;
import com.example.userservice.dto.OrderResponse;
import com.example.userservice.dto.UserRequest;
import com.example.userservice.dto.UserResponse;
import com.example.userservice.entity.User;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.security.PrincipalDetails;
import jakarta.ws.rs.NotFoundException;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final OrderServiceClient orderServiceClient;
    private final Resilience4JCircuitBreakerFactory circuitBreakerFactory;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new NotFoundException("User not found")
        );

        return new PrincipalDetails(user);
    }

    @Override
    public UserResponse.Summary createUser(UserRequest.Signup userRequest) {
        String encodedPassword = passwordEncoder.encode(userRequest.password());

        User newUser = User.of(encodedPassword, userRequest);
        User savedUser = userRepository.save(newUser);

        return new UserResponse.Summary(savedUser);
    }

    @Override
    public UserResponse.Get getUserByUserId(String userId) {
        User user = userRepository.findByUserId(userId).orElseThrow(
                () -> new NotFoundException("User not found")
        );

        CircuitBreaker circuitBreaker = circuitBreakerFactory.create("circuitbreaker");
        OrderResponse.Paged orderList = circuitBreaker.run(
                () -> orderServiceClient.getOrderList(userId),
                throwable -> new OrderResponse.Paged(
                        new ArrayList<>(),
                        0,
                        0,
                        0,
                        0
                )
        );

        return new UserResponse.Get(user, orderList);
    }

    @Override
    public Page<UserResponse.Summary> getUserByAll(int page, int size) {
        return userRepository.findAll(PageRequest.of(page - 1, size))
                .map(UserResponse.Summary::new);
    }
}
