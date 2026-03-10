package com.ecommerce.spring.entity;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "address")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long addressId;

    @NotBlank(message = "Street name cannot be blank")
    private String street;

    @NotBlank(message = "Building cannot be empty")
    private String building;

    @NotBlank(message = "State cannot be empty")
    private String state;

    @NotBlank(message = "City cannot be empty")
    private String city;

    @NotBlank(message = "Pincode cannot be empty")
    @Size(min = 6, message = "Pincode must have 6 numbers")
    private String pincode;

    @ToString.Exclude
    @ManyToMany(mappedBy = "address")
    private List<User> users;

}
