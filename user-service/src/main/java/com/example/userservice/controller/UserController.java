package com.example.userservice.controller;

import com.example.userservice.dto.UserRequest;
import com.example.userservice.dto.UserResponse;
import com.example.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class UserController {

    private final Environment env;
    private final UserService userService;

    @GetMapping("/test")
    public String test() {
        return String.format("It's Working in User Service"
                + ", port(local.server.port)=" + env.getProperty("local.server.port")
                + ", port(server.port)=" + env.getProperty("server.port")
                + ", access token secret key=" + env.getProperty("jwt.token.access.secret.key")
                + ", refresh token secret key=" + env.getProperty("jwt.token.refresh.secret.key"));
    }

    @PostMapping("/auth/signup")
    public ResponseEntity<UserResponse.Summary> signup(
            @RequestBody UserRequest.Signup userRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(userRequest));
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<UserResponse.Get> getUser(
            @PathVariable("userId") String userId
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getUserByUserId(userId));
    }

    @GetMapping("/users")
    public ResponseEntity<Page<UserResponse.Summary>> getUsers(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getUserByAll(page, size));
    }
}
