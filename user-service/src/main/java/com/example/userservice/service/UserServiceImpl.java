package com.example.userservice.service;

import com.example.userservice.client.OrderServiceClient;
import com.example.userservice.dto.OrderResponse;
import com.example.userservice.dto.UserRequest;
import com.example.userservice.dto.UserResponse;
import com.example.userservice.entity.User;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.security.PrincipalDetails;
import feign.FeignException;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${order.service.url}")
    private String orderServiceUrl;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final OrderServiceClient orderServiceClient;

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

        OrderResponse.Paged orderList = null;
        try {
            orderList = orderServiceClient.getOrderList(userId);
        } catch (FeignException e) {
            log.error(e.getMessage());
        }

        return new UserResponse.Get(user, orderList);
    }

    @Override
    public Page<UserResponse.Summary> getUserByAll(int page, int size) {
        return userRepository.findAll(PageRequest.of(page - 1, size))
                .map(UserResponse.Summary::new);
    }
}
