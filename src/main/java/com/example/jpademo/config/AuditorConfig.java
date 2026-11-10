package com.example.jpademo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Optional;

/**
 * 7.3 Enabling Auditing in Spring Data JPA.
 *
 * Slide version:   return () -> Optional.of("admin");
 *
 * Demo version:    reads the "X-User" request header so that createdBy and
 *                  lastModifiedBy can be shown changing between requests.
 *                    - HTTP request without header -> "admin"   (same as slide)
 *                    - HTTP request with X-User    -> that value
 *                    - no HTTP request (seeder)    -> "system"
 */
@Configuration
@EnableJpaAuditing
public class AuditorConfig {

    @Bean
    public AuditorAware<String> auditorProvider() {
        return () -> {
            if (RequestContextHolder.getRequestAttributes() instanceof ServletRequestAttributes attrs) {
                String user = attrs.getRequest().getHeader("X-User");
                return Optional.of(user != null && !user.isBlank() ? user : "admin");
            }
            return Optional.of("system");
        };
    }
}
