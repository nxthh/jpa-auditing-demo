package com.example.jpademo.service;

import com.example.jpademo.dto.ProductRequest;
import com.example.jpademo.entity.Product;
import com.example.jpademo.repository.ProductRepository;
import com.example.jpademo.specification.ProductSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository repository;

    public Product create(ProductRequest request) {
        Product p = new Product();
        p.setName(request.name());
        p.setCategory(request.category());
        p.setPrice(request.price());
        return repository.save(p);
    }

    public List<Product> findAll() {
        return repository.findAll();
    }

    /** Slide: category = 'Laptop' */
    public List<Product> findByCategory(String category) {
        return repository.findAll(ProductSpecification.categoryEquals(category));
    }

    /** Combine several small specs. Any null/blank filter is simply ignored. */
    public List<Product> search(String keyword, String category, BigDecimal min, BigDecimal max) {
        Specification<Product> spec = Specification.allOf(
                ProductSpecification.nameContains(keyword),
                ProductSpecification.categoryIs(category),
                ProductSpecification.priceBetween(min, max)
        );
        return repository.findAll(spec);
    }
}
