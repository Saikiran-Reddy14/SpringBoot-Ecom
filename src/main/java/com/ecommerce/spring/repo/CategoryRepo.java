package com.ecommerce.spring.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.spring.model.Category;

public interface CategoryRepo extends JpaRepository<Category, Long> {

}
