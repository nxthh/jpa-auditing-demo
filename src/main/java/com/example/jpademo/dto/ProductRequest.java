package com.example.jpademo.dto;

import java.math.BigDecimal;

public record ProductRequest(String name, String category, BigDecimal price) {
}
