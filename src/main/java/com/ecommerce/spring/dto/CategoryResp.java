package com.ecommerce.spring.dto;

import lombok.Builder;

@Builder
public record CategoryResp(
        Long categoryId,
        String categoryName) {
}
