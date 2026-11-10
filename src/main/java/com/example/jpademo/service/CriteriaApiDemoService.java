package com.example.jpademo.service;

import com.example.jpademo.entity.Student;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 8.2 Understanding the JPA Criteria API - the raw API, no Spring Data on top.
 * Components: CriteriaBuilder, CriteriaQuery, Root, Predicate.
 */
@Service
@RequiredArgsConstructor
public class CriteriaApiDemoService {

    private final EntityManager entityManager;

    /** SELECT * FROM students WHERE age > :age */
    @Transactional(readOnly = true)
    public List<Student> olderThan(int age) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();             // 1. factory
        CriteriaQuery<Student> cq = cb.createQuery(Student.class);           // 2. the query
        Root<Student> root = cq.from(Student.class);                         // 3. FROM students

        Predicate condition = cb.greaterThan(root.<Integer>get("age"), age); // 4. WHERE age > ?

        cq.select(root).where(condition);

        return entityManager.createQuery(cq).getResultList();
    }

    /**
     * SELECT * FROM students
     * WHERE score >= :minScore AND major = :major
     * ORDER BY score DESC;
     */
    @Transactional(readOnly = true)
    public List<Student> topStudentsByMajor(String major, int minScore) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Student> cq = cb.createQuery(Student.class);
        Root<Student> root = cq.from(Student.class);

        cq.select(root)
          .where(cb.and(
                  cb.greaterThanOrEqualTo(root.<Integer>get("score"), minScore),
                  cb.equal(root.get("major"), major)))
          .orderBy(cb.desc(root.get("score")));

        return entityManager.createQuery(cq).getResultList();
    }
}
