package com.example.jpademo.dto;

public record StudentRequest(
        String name,
        String email,
        Integer age,
        String gender,
        String major,
        Integer score
) {
}
