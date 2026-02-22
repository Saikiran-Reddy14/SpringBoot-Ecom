package com.ecommerce.spring.controller;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.spring.dto.ApiResponse;
import com.ecommerce.spring.dto.CategoryRequest;
import com.ecommerce.spring.dto.CategoryResp;
import com.ecommerce.spring.dto.CategoryResponse;
import com.ecommerce.spring.service.CategoryService;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api")
@Validated
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/public/categories")
    public ResponseEntity<ApiResponse<CategoryResponse>> getCategories(
            @RequestParam(name = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = "10", required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = "categoryId", required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = "asc", required = false) String sortOrder) {
        CategoryResponse categories = categoryService.getAllCategories(pageNumber, pageSize, sortBy, sortOrder);
        ApiResponse<CategoryResponse> response = new ApiResponse<>(
                "Categories fetched successfully", HttpStatus.OK.value(), categories);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/admin/categories")
    public ResponseEntity<ApiResponse<CategoryResp>> saveCategory(@Valid @RequestBody CategoryRequest request) {
        CategoryResp saved = categoryService.saveCategory(request.getCategoryName());
        ApiResponse<CategoryResp> response = new ApiResponse<>(
                "Category created successfully", HttpStatus.CREATED.value(), saved);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/public/categories/{id}")
    public ResponseEntity<ApiResponse<CategoryResp>> getCategoryById(@PathVariable Long id) {
        CategoryResp category = categoryService.getCategoryById(id);
        ApiResponse<CategoryResp> response = new ApiResponse<>(
                "Category fetched successfully", HttpStatus.OK.value(), category);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/admin/categories/{id}")
    public ResponseEntity<ApiResponse<CategoryResp>> updateCategory(@PathVariable Long id,
            @Valid @RequestBody CategoryRequest request) {
        CategoryResp updated = categoryService.updateCategory(id, request.getCategoryName());
        ApiResponse<CategoryResp> response = new ApiResponse<>(
                "Category updated successfully", HttpStatus.OK.value(), updated);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/admin/categories/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        categoryService.delete(id);
        ApiResponse<Void> response = new ApiResponse<>(
                "Category deleted successfully", HttpStatus.OK.value(), null);
        return ResponseEntity.ok(response);
    }

}
