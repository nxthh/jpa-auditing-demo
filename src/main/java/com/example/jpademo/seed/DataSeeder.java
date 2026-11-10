package com.example.jpademo.seed;

import com.example.jpademo.entity.Category;
import com.example.jpademo.entity.Product;
import com.example.jpademo.entity.Student;
import com.example.jpademo.repository.CategoryRepository;
import com.example.jpademo.repository.ProductRepository;
import com.example.jpademo.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

/** Seeds sample data on startup (audited as "system" because there is no HTTP request). */
@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final StudentRepository students;
    private final ProductRepository products;
    private final CategoryRepository categories;

    @Override
    public void run(String... args) {
        if (students.count() > 0) return;

        students.saveAll(List.of(
                student("Dara",   "dara@gmail.com",   22, "Male",   "IT",       85),
                student("Sokha",  "sokha@gmail.com",  19, "Female", "IT",       72),
                student("Vanna",  "vanna@gmail.com",  24, "Male",   "Business", 64),
                student("Sreyka", "sreyka@gmail.com", 21, "Female", "Business", 91),
                student("Rithy",  "rithy@gmail.com",  20, "Male",   "IT",       45),
                student("Maly",   "maly@gmail.com",   23, "Female", "Design",    58),
                student("Darany", "darany@gmail.com", 18, "Female", "IT",       77)
        ));

        categories.saveAll(List.of(
                category("Laptop"), category("Phone"), category("Accessory")
        ));

        products.saveAll(List.of(
                product("MacBook Air",    "Laptop",    "1099.00"),
                product("ThinkPad X1",    "Laptop",    "1499.00"),
                product("Dell XPS 13",    "Laptop",    "999.00"),
                product("iPhone 16",      "Phone",     "899.00"),
                product("Galaxy S25",     "Phone",     "799.00"),
                product("USB-C Charger",  "Accessory", "29.00"),
                product("Wireless Mouse", "Accessory", "39.00")
        ));
    }

    private Student student(String name, String email, int age, String gender, String major, int score) {
        Student s = new Student();
        s.setName(name);
        s.setEmail(email);
        s.setAge(age);
        s.setGender(gender);
        s.setMajor(major);
        s.setScore(score);
        return s;
    }

    private Category category(String name) {
        Category c = new Category();
        c.setCategoryName(name);
        return c;
    }

    private Product product(String name, String category, String price) {
        Product p = new Product();
        p.setName(name);
        p.setCategory(category);
        p.setPrice(new BigDecimal(price));
        return p;
    }
}
