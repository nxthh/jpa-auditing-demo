package com.example.jpademo.controller;

import com.example.jpademo.dto.ProductRequest;
import com.example.jpademo.entity.Product;
import com.example.jpademo.service.ProductService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Tag(name = "Products")
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Product create(@RequestBody ProductRequest request) {
        return service.create(request);
    }

    @GetMapping
    public List<Product> findAll() {
        return service.findAll();
    }

    /** Slide: WHERE category = 'Laptop' */
    @GetMapping("/by-category")
    public List<Product> byCategory(@RequestParam String category) {
        return service.findByCategory(category);
    }

    @GetMapping("/search")
    public List<Product> search(@RequestParam(required = false) String keyword,
                                @RequestParam(required = false) String category,
                                @RequestParam(required = false) BigDecimal minPrice,
                                @RequestParam(required = false) BigDecimal maxPrice) {
        return service.search(keyword, category, minPrice, maxPrice);
    }
}
