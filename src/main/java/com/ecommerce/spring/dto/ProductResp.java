package com.ecommerce.spring.dto;

import java.math.BigDecimal;

import lombok.Builder;

@Builder
public record ProductResp(
        Long productId,
        String productName,
        String description,
        String image,
        Integer quantity,
        BigDecimal price,
        BigDecimal specialPrice,
        BigDecimal discount) {
}
