package com.ecommerce.spring.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.ecommerce.spring.dto.AllProducts;
import com.ecommerce.spring.dto.ProductReq;
import com.ecommerce.spring.dto.ProductResp;
import com.ecommerce.spring.exception.ResourceExistsException;
import com.ecommerce.spring.exception.ResourceNotFoundException;
import com.ecommerce.spring.model.Category;
import com.ecommerce.spring.model.Product;
import com.ecommerce.spring.repo.CategoryRepo;
import com.ecommerce.spring.repo.ProductRepo;

@Service
public class ProductService {

    private ProductRepo productRepo;
    private CategoryRepo categoryRepo;

    @Value("${project.image-upload-dir}")
    private String imageUploadDir;

    public ProductService(ProductRepo productRepo, CategoryRepo categoryRepo) {
        this.productRepo = productRepo;
        this.categoryRepo = categoryRepo;
    }

    @Transactional
    public ProductResp addProduct(ProductReq productReq, MultipartFile image, Long categoryId) {
        Category category = categoryRepo.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Category not found with id: " + categoryId));

        if (productRepo.existsByProductName(productReq.productName())) {
            throw new ResourceExistsException("Product already exists with name: " + productReq.productName());
        }

        String imageName = uploadImage(image);

        Product product = new Product();
        product.setProductName(productReq.productName());
        product.setDescription(productReq.description());
        product.setQuantity(productReq.quantity());
        product.setPrice(productReq.price());
        product.setDiscount(productReq.discount());
        BigDecimal discountAmount = product.getPrice()
                .multiply(product.getDiscount())
                .multiply(new BigDecimal("0.01"));
        product.setSpecialPrice(product.getPrice().subtract(discountAmount).setScale(2, RoundingMode.HALF_UP));
        product.setImage(imageName);
        product.setCategory(category);

        Product savedProduct = productRepo.save(product);

        String imageUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/images/")
                .path(savedProduct.getImage())
                .toUriString();

        return ProductResp.builder()
                .productId(savedProduct.getProductId())
                .productName(savedProduct.getProductName())
                .description(savedProduct.getDescription())
                .image(imageUrl)
                .quantity(savedProduct.getQuantity())
                .price(savedProduct.getPrice())
                .specialPrice(savedProduct.getSpecialPrice())
                .discount(savedProduct.getDiscount())
                .categoryId(savedProduct.getCategory().getCategoryId())
                .categoryName(savedProduct.getCategory().getCategoryName())
                .build();
    }

    @Transactional(readOnly = true)
    public ProductResp getProduct(Long id) {
        Product product = productRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product does not exists with id: " + id));

        String imageUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/images/")
                .path(product.getImage())
                .toUriString();

        return ProductResp.builder()
                .productId(product.getProductId())
                .productName(product.getProductName())
                .description(product.getDescription())
                .image(imageUrl)
                .quantity(product.getQuantity())
                .price(product.getPrice())
                .specialPrice(product.getSpecialPrice())
                .discount(product.getDiscount())
                .categoryId(product.getCategory().getCategoryId())
                .categoryName(product.getCategory().getCategoryName())
                .build();
    }

    @Transactional(readOnly = true)
    public AllProducts getProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sort = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable page = PageRequest.of(pageNumber, pageSize, sort);
        Page<Product> pageData = productRepo.findAll(page);

        List<ProductResp> productResps = pageData.getContent().stream()
                .map(product -> {
                    String imageUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                            .path("/images/")
                            .path(product.getImage())
                            .toUriString();
                    return ProductResp.builder()
                            .productId(product.getProductId())
                            .productName(product.getProductName())
                            .description(product.getDescription())
                            .image(imageUrl)
                            .quantity(product.getQuantity())
                            .price(product.getPrice())
                            .specialPrice(product.getSpecialPrice())
                            .discount(product.getDiscount())
                            .categoryId(product.getCategory().getCategoryId())
                            .categoryName(product.getCategory().getCategoryName())
                            .build();
                })
                .toList();

        return AllProducts.builder()
                .products(productResps)
                .pageNumber(pageData.getNumber())
                .pageSize(pageData.getSize())
                .totalElements(pageData.getTotalElements())
                .totalPages(pageData.getTotalPages())
                .lastPage(pageData.isLast())
                .build();
    }

    @Transactional(readOnly = true)
    public AllProducts getProductsByCategory(Long categoryId,
            Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        boolean exists = categoryRepo.existsByCategoryId(categoryId);
        if (!exists) {
            throw new ResourceNotFoundException("Category does not exist with id " + categoryId);
        }
        Sort sort = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable page = PageRequest.of(pageNumber, pageSize, sort);
        Page<Product> pageData = productRepo.findByCategory_CategoryId(categoryId, page);
        List<ProductResp> products = pageData.getContent().stream()
                .map(product -> {
                    String imageUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                            .path("/images/")
                            .path(product.getImage())
                            .toUriString();
                    return ProductResp.builder()
                            .productId(product.getProductId())
                            .productName(product.getProductName())
                            .description(product.getDescription())
                            .image(imageUrl)
                            .quantity(product.getQuantity())
                            .price(product.getPrice())
                            .specialPrice(product.getSpecialPrice())
                            .discount(product.getDiscount())
                            .categoryId(product.getCategory().getCategoryId())
                            .categoryName(product.getCategory().getCategoryName())
                            .build();
                }).toList();
        return AllProducts.builder()
                .products(products)
                .pageNumber(pageData.getNumber())
                .pageSize(pageData.getSize())
                .totalElements(pageData.getTotalElements())
                .totalPages(pageData.getTotalPages())
                .lastPage(pageData.isLast())
                .build();
    }

    @Transactional(readOnly = true)
    public AllProducts getProductsByKeyword(String word, Integer pageNumber, Integer pageSize, String sortBy,
            String sortOrder) {

        Sort sort = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable page = PageRequest.of(pageNumber, pageSize, sort);
        Page<Product> pageData = productRepo.findByProductNameContainingIgnoreCase(word, page);
        List<ProductResp> productResps = pageData.getContent().stream()
                .map(product -> {
                    String imageUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                            .path("/images/")
                            .path(product.getImage())
                            .toUriString();
                    return ProductResp.builder()
                            .productId(product.getProductId())
                            .productName(product.getProductName())
                            .description(product.getDescription())
                            .image(imageUrl)
                            .quantity(product.getQuantity())
                            .price(product.getPrice())
                            .specialPrice(product.getSpecialPrice())
                            .discount(product.getDiscount())
                            .categoryId(product.getCategory().getCategoryId())
                            .categoryName(product.getCategory().getCategoryName())
                            .build();
                })
                .toList();

        return AllProducts.builder()
                .products(productResps)
                .pageNumber(pageData.getNumber())
                .pageSize(pageData.getSize())
                .totalElements(pageData.getTotalElements())
                .totalPages(pageData.getTotalPages())
                .lastPage(pageData.isLast())
                .build();
    }

    private String uploadImage(MultipartFile image) {
        String originalFilename = image.getOriginalFilename();
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }

        String uniqueFilename = UUID.randomUUID().toString() + extension;

        try {
            Path uploadPath = Paths.get(imageUploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            Path filePath = uploadPath.resolve(uniqueFilename);
            Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload image: " + e.getMessage(), e);
        }

        return uniqueFilename;
    }
}
