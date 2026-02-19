package com.ecommerce.spring.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ecommerce.spring.exception.ResourceNotFoundException;
import com.ecommerce.spring.model.Category;
import com.ecommerce.spring.repo.CategoryRepo;

import org.springframework.transaction.annotation.Transactional;

@Service
public class CategoryService {

    private final CategoryRepo categoryRepo;

    public CategoryService(CategoryRepo categoryRepo) {
        this.categoryRepo = categoryRepo;
    }

    @Transactional(readOnly = true)
    public List<Category> getAllCategories() {
        return categoryRepo.findAll();
    }

    @Transactional
    public void delete(Long id) {
        Category category = categoryRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", id));
        categoryRepo.delete(category);
    }

    public Category saveCategory(Category category) {
        return categoryRepo.save(category);
    }

    public Category getCategoryById(Long id) {
        return categoryRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", id));
    }

    public Category updateCategory(Long id, Category updatedCategory) {
        Category category = categoryRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", id));
        category.setCategoryName(updatedCategory.getCategoryName());
        return categoryRepo.save(category);
    }

}
