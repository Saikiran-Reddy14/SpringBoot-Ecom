package com.ecommerce.spring.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ecommerce.spring.exception.ResourceExistsException;
import com.ecommerce.spring.exception.ResourceNotFoundException;
import com.ecommerce.spring.model.Category;
import com.ecommerce.spring.repo.CategoryRepo;

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

    @Transactional
    public Category saveCategory(String categoryName) {
        Optional<Category> categoryExists = categoryRepo.findByCategoryName(categoryName.trim());
        if (categoryExists.isPresent()) {
            throw new ResourceExistsException("Category already exists with name: " + categoryName);
        }
        Category category = new Category();
        category.setCategoryName(categoryName.trim());
        return categoryRepo.save(category);
    }

    @Transactional(readOnly = true)
    public Category getCategoryById(Long id) {
        return categoryRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", id));
    }

    @Transactional
    public Category updateCategory(Long id, String categoryName) {
        Category category = categoryRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", id));

        Optional<Category> categoryExists = categoryRepo.findByCategoryName(categoryName.trim());
        if (categoryExists.isPresent()) {
            throw new ResourceExistsException("Category already exists with name: " + categoryName);
        }
        category.setCategoryName(categoryName.trim());
        return categoryRepo.save(category);
    }

}
