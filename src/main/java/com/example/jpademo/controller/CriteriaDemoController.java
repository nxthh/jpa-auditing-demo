package com.example.jpademo.controller;

import com.example.jpademo.entity.Student;
import com.example.jpademo.service.CriteriaApiDemoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Criteria API")
@RestController
@RequestMapping("/api/criteria/students")
@RequiredArgsConstructor
public class CriteriaDemoController {

    private final CriteriaApiDemoService service;

    @GetMapping("/older-than")
    public List<Student> olderThan(@RequestParam(defaultValue = "20") int age) {
        return service.olderThan(age);
    }

    @GetMapping("/top")
    public List<Student> top(@RequestParam(defaultValue = "IT") String major,
                             @RequestParam(defaultValue = "50") int minScore) {
        return service.topStudentsByMajor(major, minScore);
    }
}
