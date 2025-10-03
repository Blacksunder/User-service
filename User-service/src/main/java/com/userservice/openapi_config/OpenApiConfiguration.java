package com.userservice.openapi_config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
public class OpenApiConfiguration {

    @Bean
    public OpenAPI openAPI() {
        Info info = new Info()
                .title("API for users' accounts processing")
                .description("Provides saving, updating, getting and deleting accounts functions");
        return new OpenAPI().info(info);
    }
}
