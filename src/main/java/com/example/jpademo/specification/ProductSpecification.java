package com.example.jpademo.specification;

import com.example.jpademo.entity.Product;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

public class ProductSpecification {

    private ProductSpecification() {
    }

    /**
     * 8.3 The Specification interface - the simplest possible example.
     * SQL: SELECT * FROM products WHERE category = 'Laptop';
     */
    public static Specification<Product> categoryEquals(String category) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("category"), category);
    }

    public static Specification<Product> nameContains(String keyword) {
        return (root, query, cb) -> (keyword == null || keyword.isBlank())
                ? null
                : cb.like(cb.lower(root.<String>get("name")), "%" + keyword.toLowerCase() + "%");
    }

    public static Specification<Product> categoryIs(String category) {
        return (root, query, cb) -> (category == null || category.isBlank())
                ? null
                : cb.equal(root.get("category"), category);
    }

    public static Specification<Product> priceBetween(BigDecimal min, BigDecimal max) {
        return (root, query, cb) -> {
            if (min != null && max != null) {
                return cb.between(root.<BigDecimal>get("price"), min, max);
            }
            if (min != null) {
                return cb.greaterThanOrEqualTo(root.<BigDecimal>get("price"), min);
            }
            if (max != null) {
                return cb.lessThanOrEqualTo(root.<BigDecimal>get("price"), max);
            }
            return null;
        };
    }
}
