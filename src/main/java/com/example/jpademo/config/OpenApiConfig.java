package com.example.jpademo.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger UI setup. The "Authorize" button lets you type a user name once;
 * Swagger then sends it as the X-User header on every request, which our
 * AuditorAware turns into createdBy / lastModifiedBy.
 */
@Configuration
public class OpenApiConfig {

    private static final String SCHEME = "X-User";

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Spring Data JPA Demo")
                        .description("JPA Auditing + JPA Specification live demo")
                        .version("1.0"))
                .components(new Components().addSecuritySchemes(SCHEME,
                        new SecurityScheme()
                                .type(SecurityScheme.Type.APIKEY)
                                .in(SecurityScheme.In.HEADER)
                                .name("X-User")
                                .description("Auditor name (optional). Empty = admin")))
                .addSecurityItem(new SecurityRequirement().addList(SCHEME));
    }
}
