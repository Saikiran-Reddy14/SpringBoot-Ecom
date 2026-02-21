package com.ecommerce.spring.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

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
        product.setSpecialPrice(productReq.specialPrice());
        product.setDiscount(productReq.discount());
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
