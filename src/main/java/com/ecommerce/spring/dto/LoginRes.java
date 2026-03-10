package com.ecommerce.spring.dto;

import com.ecommerce.spring.entity.AppRole;

import lombok.Builder;

@Builder
public record LoginRes(
        String accessToken,
        String refreshToken,
        AppRole role) {

}
