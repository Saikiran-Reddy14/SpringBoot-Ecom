package com.ecommerce.spring.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ecommerce.spring.dto.AllProducts;
import com.ecommerce.spring.dto.ApiResponse;
import com.ecommerce.spring.dto.ProductReq;
import com.ecommerce.spring.dto.ProductResp;
import com.ecommerce.spring.service.ProductService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;

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
                                .body(new ApiResponse<>("Product created successfully", HttpStatus.CREATED.value(),
                                                savedProduct));
        }

        @GetMapping("/public/products/{productId}")
        public ResponseEntity<ApiResponse<ProductResp>> getProduct(@PathVariable Long productId) {
                ProductResp product = productService.getProduct(productId);
                return ResponseEntity.status(HttpStatus.OK)
                                .body(new ApiResponse<>("Product retrieved successfully", HttpStatus.OK.value(),
                                                product));
        }

        @GetMapping("/public/products")
        public ResponseEntity<ApiResponse<AllProducts>> getProducts(
                        @RequestParam(name = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
                        @RequestParam(name = "pageSize", defaultValue = "10", required = false) Integer pageSize,
                        @RequestParam(name = "sortBy", defaultValue = "productName", required = false) String sortBy,
                        @RequestParam(name = "sortOrder", defaultValue = "asc", required = false) String sortOrder) {
                AllProducts products = productService.getProducts(pageNumber, pageSize, sortBy, sortOrder);

                return ResponseEntity.status(HttpStatus.OK)
                                .body(new ApiResponse<>("Products retrieved successfully", HttpStatus.OK.value(),
                                                products));
        }

        @GetMapping("/public/categories/{categoryId}/products")
        public ResponseEntity<ApiResponse<AllProducts>> getProductsByCategory(@PathVariable Long categoryId,
                        @RequestParam(name = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
                        @RequestParam(name = "pageSize", defaultValue = "10", required = false) Integer pageSize,
                        @RequestParam(name = "sortBy", defaultValue = "productName", required = false) String sortBy,
                        @RequestParam(name = "sortOrder", defaultValue = "asc", required = false) String sortOrder) {
                AllProducts products = productService.getProductsByCategory(categoryId, pageNumber, pageSize, sortBy,
                                sortOrder);
                return ResponseEntity.status(HttpStatus.OK)
                                .body(new ApiResponse<>("Products retrieved successfully", HttpStatus.OK.value(),
                                                products));
        }

        @GetMapping("/public/products/keyword/{word}")
        public ResponseEntity<ApiResponse<AllProducts>> getProductsByKeyword(@PathVariable String word,
                        @RequestParam(name = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
                        @RequestParam(name = "pageSize", defaultValue = "10", required = false) Integer pageSize,
                        @RequestParam(name = "sortBy", defaultValue = "productName", required = false) String sortBy,
                        @RequestParam(name = "sortOrder", defaultValue = "asc", required = false) String sortOrder) {
                AllProducts products = productService.getProductsByKeyword(word, pageNumber, pageSize, sortBy,
                                sortOrder);
                return ResponseEntity.status(HttpStatus.OK)
                                .body(new ApiResponse<>("Products retrieved successfully", HttpStatus.OK.value(),
                                                products));
        }

        @PatchMapping("/admin/products/{productId}")
        public ResponseEntity<ApiResponse<ProductResp>> updateProduct(
                        @RequestBody ProductReq productReq,
                        @PathVariable Long productId) {
                ProductResp updatedProduct = productService.updateProduct(productId, productReq);
                return ResponseEntity.status(HttpStatus.OK)
                                .body(new ApiResponse<>("Product updated successfully", HttpStatus.OK.value(),
                                                updatedProduct));
        }

        @PatchMapping("/admin/products/{productId}/image")
        public ResponseEntity<ApiResponse<ProductResp>> updateProductImage(
                        @PathVariable Long productId,
                        @RequestParam("image") MultipartFile image) {
                ProductResp updatedProduct = productService.updateProductImage(productId, image);
                return ResponseEntity.status(HttpStatus.OK)
                                .body(new ApiResponse<>("Product image updated successfully", HttpStatus.OK.value(),
                                                updatedProduct));
        }

        @DeleteMapping("/admin/products/{productId}")
        public ResponseEntity<ApiResponse<Object>> deleteProduct(@PathVariable Long productId) {
                String message = productService.deleteProduct(productId);
                return ResponseEntity.status(HttpStatus.OK)
                                .body(new ApiResponse<>(message, HttpStatus.OK.value(), null));
        }

}
