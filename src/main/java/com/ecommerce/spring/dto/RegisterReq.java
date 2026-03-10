package com.ecommerce.spring.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record RegisterReq(@NotBlank(message = "Username is required") String username,
        @NotBlank(message = "Email is required") @Email(message = "Please provide a valid email address") String email,
        @NotBlank(message = "Password is required") @Min(value = 6, message = "Password must be at least 6 characters long") String password) {

}
