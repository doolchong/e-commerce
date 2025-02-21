package com.example.userservice.dto;

import com.example.userservice.dto.UserRequest.Login;
import com.example.userservice.dto.UserRequest.Signup;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public sealed interface UserRequest permits Signup, Login {

    record Signup(
            @Email
            @NotNull(message = "Email cannot be null")
            @Size(min = 2, message = "Email not be less than two characters")
            String email,

            @NotNull(message = "Name cannot be null")
            @Size(min = 2, message = "Name not be less than two characters")
            String name,

            @NotNull(message = "Password cannot be null")
            @Size(min = 8, message = "Password must equal or grater than 8 characters")
            String password
    ) implements UserRequest {

    }

    record Login(
            @Email
            @NotNull(message = "Email cannot be null")
            @Size(min = 2, message = "Email not be less than two characters")
            String email,

            @NotNull(message = "Password cannot be null")
            @Size(min = 8, message = "Password must equal or grater than 8 characters")
            String password
    ) implements UserRequest {

    }
}