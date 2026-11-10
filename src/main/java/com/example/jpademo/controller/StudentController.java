package com.example.jpademo.controller;

import com.example.jpademo.dto.PageResponse;
import com.example.jpademo.dto.StudentRequest;
import com.example.jpademo.entity.Student;
import com.example.jpademo.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService service;

    // ---------- Auditing demo ----------

    /** Slide example: creates "Dara". Check createdDate / createdBy in the response. */
    @PostMapping("/demo")
    @ResponseStatus(HttpStatus.CREATED)
    public Student createDemoStudent() {
        return service.createStudent();
    }

    /** Send header "X-User: someone" to change the auditor. */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Student create(@RequestBody StudentRequest request) {
        return service.create(request);
    }

    @PutMapping("/{id}")
    public Student update(@PathVariable Long id, @RequestBody StudentRequest request) {
        return service.update(id, request);
    }

    @GetMapping
    public List<Student> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public Student findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    // ---------- Specification demo ----------

    /** BEFORE: derived queries (needs a method per combination). */
    @GetMapping("/search/derived")
    public List<Student> searchDerived(@RequestParam(required = false) String name,
                                       @RequestParam(required = false) Integer age) {
        return service.searchWithDerivedQueries(name, age);
    }

    /** AFTER: one dynamic Specification (slide version). */
    @GetMapping("/search")
    public List<Student> search(@RequestParam(required = false) String name,
                                @RequestParam(required = false) String gender,
                                @RequestParam(required = false) Integer age) {
        return service.search(name, gender, age);
    }

    /** ADVANCED: composed specs + paging + sorting. */
    @GetMapping("/search/advanced")
    public PageResponse<Student> searchAdvanced(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String gender,
            @RequestParam(required = false) Integer age,
            @RequestParam(required = false) String major,
            @RequestParam(required = false) Integer minScore,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") Sort.Direction direction) {
        return service.searchAdvanced(name, gender, age, major, minScore, page, size, sortBy, direction);
    }
}
