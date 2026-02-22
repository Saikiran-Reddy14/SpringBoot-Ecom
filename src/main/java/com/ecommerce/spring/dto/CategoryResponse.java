package com.ecommerce.spring.dto;

import java.util.List;

import lombok.Builder;

@Builder
public record CategoryResponse(
        List<CategoryResp> content,
        Integer pageNumber,
        Integer pageSize,
        Long totalElements,
        Integer totalPages,
        Boolean lastPage) {
}
