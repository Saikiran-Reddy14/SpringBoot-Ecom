package com.ecommerce.spring.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ecommerce.spring.dto.CategoryResponse;
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
    public CategoryResponse getAllCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sort = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable page = PageRequest.of(pageNumber, pageSize, sort);
        Page<Category> pageData = categoryRepo.findAll(page);

        CategoryResponse response = CategoryResponse.builder()
                .content(pageData.getContent())
                .pageNumber(pageData.getNumber())
                .pageSize(pageData.getSize())
                .totalElements(pageData.getTotalElements())
                .totalPages(pageData.getTotalPages())
                .lastPage(pageData.isLast())
                .build();
        return response;
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
