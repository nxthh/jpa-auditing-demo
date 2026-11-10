package com.example.jpademo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "products")
public class Product extends BaseEntity {

    private String name;

    // kept as a plain String so the slide example
    // root.get("category") == "Laptop" works as-is
    private String category;

    private BigDecimal price;
}
