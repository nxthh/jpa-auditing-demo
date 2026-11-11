package com.example.jpademo.controller;

import com.example.jpademo.entity.Category;
import com.example.jpademo.repository.CategoryRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/** Category shares BaseEntity with Student and Product -> same audit columns, zero repetition. */
@Tag(name = "Categories")
@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryRepository repository;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Category create(@RequestBody Category category) {
        category.setId(null);
        return repository.save(category);
    }

    @GetMapping
    public List<Category> findAll() {
        return repository.findAll();
    }
}
