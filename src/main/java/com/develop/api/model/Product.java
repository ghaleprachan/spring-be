package com.develop.api.model;

import jakarta.persistence.*;
import lombok.Data;
import jakarta.validation.constraints.*;


@Data
@Entity
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Name cannot be empty")
    private String name;

    private String description;

    @Min(value = 0, message = "Count cannot be negative")
    private int count;

    @Email(message = "Owner email must be valid")
    private String ownerEmail;
}
