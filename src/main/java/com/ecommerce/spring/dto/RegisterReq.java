package com.ecommerce.spring.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterReq(@NotBlank(message = "Username is required") String username,
        @NotBlank(message = "Email is required") @Email(message = "Please provide a valid email address") String email,
        @NotBlank(message = "Password is required") @Size(min = 8, message = "Password must be at least 8 characters long") String password) {

}
