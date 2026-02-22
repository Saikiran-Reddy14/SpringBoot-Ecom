package com.ecommerce.spring.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ecommerce.spring.model.Product;

@Repository
public interface ProductRepo extends JpaRepository<Product, Long> {

    boolean existsByProductName(String productName);

    Page<Product> findByCategory_CategoryId(Long categoryId, Pageable pageable);

    Page<Product> findByProductNameContainingIgnoreCase(String keyword, Pageable pageable);

}
