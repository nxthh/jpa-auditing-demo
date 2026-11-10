package com.example.jpademo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "students")
public class Student extends BaseEntity {

    private String name;
    private String email;
    private Integer age;
    private String gender;
    private String major;
    private Integer score;
}
