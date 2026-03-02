package com.ecommerce.spring.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @NotBlank(message = "Username cannot be empty")
    @Column(unique = true, nullable = false)
    @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
    private String userName;
    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Please provide a valid email address")
    @Column(unique = true, nullable = false)
    private String email;
    @Column(nullable = false)
    @NotBlank(message = "Password cannot be empty")
    @Size(min = 8, max = 20, message = "Password should contain a minimum of 8 characters and maximum of 20 characters")
    private String password;
}
