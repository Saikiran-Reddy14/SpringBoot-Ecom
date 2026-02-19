package com.ecommerce.spring.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.spring.dto.ApiResponse;
import com.ecommerce.spring.model.Category;
import com.ecommerce.spring.service.CategoryService;

@RestController
@RequestMapping("/api")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/public/categories")
    public ResponseEntity<ApiResponse<List<Category>>> getCategories() {
        List<Category> categories = categoryService.getAllCategories();
        ApiResponse<List<Category>> response = new ApiResponse<>(
                "Categories fetched successfully", HttpStatus.OK.value(), categories);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/public/categories")
    public ResponseEntity<ApiResponse<Category>> saveCategory(@RequestBody Category category) {
        Category saved = categoryService.saveCategory(category);
        ApiResponse<Category> response = new ApiResponse<>(
                "Category created successfully", HttpStatus.CREATED.value(), saved);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/public/categories/{id}")
    public ResponseEntity<ApiResponse<Category>> getCategoryById(@PathVariable Long id) {
        Category category = categoryService.getCategoryById(id);
        ApiResponse<Category> response = new ApiResponse<>(
                "Category fetched successfully", HttpStatus.OK.value(), category);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/public/categories/{id}")
    public ResponseEntity<ApiResponse<Category>> updateCategory(@PathVariable Long id,
            @RequestBody Category category) {
        Category updated = categoryService.updateCategory(id, category);
        ApiResponse<Category> response = new ApiResponse<>(
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
