package com.example.jpademo.service;

import com.example.jpademo.dto.PageResponse;
import com.example.jpademo.dto.StudentRequest;
import com.example.jpademo.entity.Student;
import com.example.jpademo.exception.ResourceNotFoundException;
import com.example.jpademo.repository.StudentRepository;
import com.example.jpademo.specification.StudentSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository repository;

    // ---------------- Auditing (section 7) ----------------

    /** Exactly the slide example: createdDate/createdBy get filled automatically. */
    public Student createStudent() {
        Student student = new Student();
        student.setName("Dara");
        student.setEmail("dara@gmail.com");
        return repository.save(student);
    }

    public Student create(StudentRequest request) {
        Student student = new Student();
        apply(student, request);
        return repository.save(student);
    }

    /** Updating changes lastModifiedDate / lastModifiedBy but NOT createdDate / createdBy. */
    @Transactional
    public Student update(Long id, StudentRequest request) {
        Student student = findById(id);
        apply(student, request);
        return repository.save(student);
    }

    public Student findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student " + id + " not found"));
    }

    public List<Student> findAll() {
        return repository.findAll();
    }

    public void delete(Long id) {
        repository.delete(findById(id));
    }

    // ---------------- Specification (section 8) ----------------

    /** 8.1 The "before": one repository method per filter combination. */
    public List<Student> searchWithDerivedQueries(String name, Integer age) {
        if (name != null && age != null) return repository.findByNameAndAge(name, age);
        if (name != null) return repository.findByName(name);
        if (age != null) return repository.findByAge(age);
        return repository.findAll();
    }

    /** Slide version: StudentSpecification.search(...) + repository.findAll(spec). */
    public List<Student> search(String name, String gender, Integer age) {
        Specification<Student> spec = StudentSpecification.search(name, gender, age);
        return repository.findAll(spec);
    }

    /** Advanced: composed specs + pagination + sorting. */
    public PageResponse<Student> searchAdvanced(String name, String gender, Integer age,
                                                String major, Integer minScore,
                                                int page, int size,
                                                String sortBy, Sort.Direction direction) {
        Specification<Student> spec =
                StudentSpecification.composed(name, gender, age, major, minScore);

        PageRequest pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        return PageResponse.from(repository.findAll(spec, pageable));
    }

    private void apply(Student s, StudentRequest r) {
        s.setName(r.name());
        s.setEmail(r.email());
        s.setAge(r.age());
        s.setGender(r.gender());
        s.setMajor(r.major());
        s.setScore(r.score());
    }
}
