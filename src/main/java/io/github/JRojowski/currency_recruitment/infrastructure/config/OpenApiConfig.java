package io.github.JRojowski.currency_recruitment.infrastructure.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info().title("Currency Recruitment"))
                .components(new Components().addSecuritySchemes("basicAuth", createSecurityScheme()))
                .addSecurityItem(new SecurityRequirement().addList("basicAuth"));
    }

    private SecurityScheme createSecurityScheme() {
        return new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("basic");
    }
}
