package com.ecommerce.spring.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

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
    public String delete(Long id) {
        Optional<Category> category = categoryRepo.findById(id);
        if (category.isPresent()) {
            categoryRepo.delete(category.get());
            return "Category deleted";
        }
        return "Category not found";
    }

    public Category saveCategory(Category category) {
        return categoryRepo.save(category);
    }

    public Optional<Category> getCategoryById(Long id) {
        return categoryRepo.findById(id);
    }

    public Category updateCategory(Long id, Category updatedCategory) {
        Optional<Category> existing = categoryRepo.findById(id);
        if (existing.isPresent()) {
            Category category = existing.get();
            category.setCategoryName(updatedCategory.getCategoryName());
            return categoryRepo.save(category);
        }
        return null;
    }

}
