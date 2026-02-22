package com.ecommerce.spring.dto;

import java.util.List;

import lombok.Builder;

@Builder
public record AllProducts(
                List<ProductResp> products,
                Integer pageNumber,
                Integer pageSize,
                Long totalElements,
                Integer totalPages,
                Boolean lastPage) {

}
