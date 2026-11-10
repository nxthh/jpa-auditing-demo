package com.example.jpademo.specification;

import com.example.jpademo.entity.Student;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

/**
 * 8.4 Building dynamic queries with advanced specifications.
 */
public class StudentSpecification {

    private StudentSpecification() {
    }

    /**
     * Slide version: ONE specification that builds a predicate list and
     * only adds a condition when the filter value is present.
     *
     * name   -> case-insensitive LIKE %name%
     * gender -> equals
     * age    -> age >= value
     */
    public static Specification<Student> search(String name, String gender, Integer age) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (name != null && !name.isBlank()) {
                predicates.add(
                        cb.like(
                                cb.lower(root.<String>get("name")),
                                "%" + name.toLowerCase() + "%"
                        )
                );
            }

            if (gender != null && !gender.isBlank()) {
                predicates.add(cb.equal(root.get("gender"), gender));
            }

            if (age != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.<Integer>get("age"), age));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    // ---------------------------------------------------------------
    // Best practice: small, single-purpose, reusable specifications
    // that are composed with and() / or() / not() / allOf().
    // A null predicate means "ignore this filter".
    // ---------------------------------------------------------------

    public static Specification<Student> nameContains(String name) {
        return (root, query, cb) -> (name == null || name.isBlank())
                ? null
                : cb.like(cb.lower(root.<String>get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<Student> hasGender(String gender) {
        return (root, query, cb) -> (gender == null || gender.isBlank())
                ? null
                : cb.equal(root.get("gender"), gender);
    }

    public static Specification<Student> ageAtLeast(Integer age) {
        return (root, query, cb) -> age == null
                ? null
                : cb.greaterThanOrEqualTo(root.<Integer>get("age"), age);
    }

    public static Specification<Student> hasMajor(String major) {
        return (root, query, cb) -> (major == null || major.isBlank())
                ? null
                : cb.equal(cb.lower(root.<String>get("major")), major.toLowerCase());
    }

    public static Specification<Student> scoreAtLeast(Integer score) {
        return (root, query, cb) -> score == null
                ? null
                : cb.greaterThanOrEqualTo(root.<Integer>get("score"), score);
    }

    /** Composed version: same result as search(...) but built from reusable blocks. */
    public static Specification<Student> composed(String name, String gender, Integer age,
                                                  String major, Integer minScore) {
        return Specification.allOf(
                nameContains(name),
                hasGender(gender),
                ageAtLeast(age),
                hasMajor(major),
                scoreAtLeast(minScore)
        );
    }
}
