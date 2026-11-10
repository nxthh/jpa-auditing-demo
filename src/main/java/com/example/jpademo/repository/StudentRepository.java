package com.example.jpademo.repository;

import com.example.jpademo.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 8.1 Why JPA Specification?
 *
 * The derived-query methods below are the "problem" slide: every new filter
 * combination needs another method (2 filters = 3 methods, 3 filters = 7 ...).
 *
 * Extending JpaSpecificationExecutor adds findAll(Specification), findAll(Specification, Pageable),
 * count(Specification), exists(Specification) ... - the "solution".
 */
@Repository
public interface StudentRepository
        extends JpaRepository<Student, Long>, JpaSpecificationExecutor<Student> {

    List<Student> findByName(String name);

    List<Student> findByAge(Integer age);

    List<Student> findByGender(String gender);

    List<Student> findByNameAndAge(String name, Integer age);

    List<Student> findByNameAndGender(String name, String gender);

    List<Student> findByAgeAndGender(Integer age, String gender);
}
