package com.ecommerce.spring.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record ProductReq(
                @NotBlank(message = "Product name cannot be empty") String productName,
                @NotBlank(message = "Description cannot be empty") String description,
                @NotNull(message = "Quantity cannot be null") @Min(value = 0, message = "Quantity cannot be negative") Integer quantity,

                @NotNull(message = "Price cannot be null") @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0") BigDecimal price,
                BigDecimal discount) {
}
