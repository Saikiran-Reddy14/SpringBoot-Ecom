package com.ecommerce.spring.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ecommerce.spring.dto.ApiResponse;
import com.ecommerce.spring.dto.ProductReq;
import com.ecommerce.spring.dto.ProductResp;
import com.ecommerce.spring.service.ProductService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class ProductController {
    private ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/admin/categories/{categoryId}/product")
    public ResponseEntity<ApiResponse<ProductResp>> addProduct(
            @Valid @RequestPart("product") ProductReq product,
            @RequestPart("image") MultipartFile image,
            @PathVariable Long categoryId) {
        ProductResp savedProduct = productService.addProduct(product, image, categoryId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>("Product created successfully", HttpStatus.CREATED.value(), savedProduct));
    }
}
