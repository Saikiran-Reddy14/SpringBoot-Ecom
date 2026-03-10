package com.ecommerce.spring.dto;

import lombok.Builder;

@Builder
public record RegisterRes(
        Long userId,
        String username,
        String email) {

}
