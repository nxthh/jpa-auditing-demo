package com.example.jpademo;

import com.example.jpademo.entity.Student;
import com.example.jpademo.repository.StudentRepository;
import com.example.jpademo.service.StudentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional   // every test rolls back, so the demo data in Postgres stays untouched
class JpaDemoApplicationTests {

    @Autowired
    StudentService service;

    @Autowired
    StudentRepository repository;

    @Test
    void auditFieldsAreFilledOnCreate() {
        Student saved = service.createStudent();

        assertNotNull(saved.getCreatedDate());
        assertNotNull(saved.getLastModifiedDate());
        // @SpringBootTest binds a mock HTTP request (no X-User header) -> auditor is "admin".
        // "system" only appears when there is no request at all (e.g. the startup DataSeeder).
        assertEquals("admin", saved.getCreatedBy());
        assertEquals("admin", saved.getLastModifiedBy());
    }

    @Test
    void specificationIgnoresMissingFilters() {
        List<Student> all = service.search(null, null, null);
        assertEquals(repository.count(), all.size());
    }

    @Test
    void specificationFiltersByNameCaseInsensitive() {
        List<Student> result = service.search("DARA", null, null);
        assertFalse(result.isEmpty());
        assertTrue(result.stream().allMatch(s -> s.getName().toLowerCase().contains("dara")));
    }
}
