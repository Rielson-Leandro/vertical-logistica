package br.com.vertical.logistica.config

import io.swagger.v3.oas.models.info.Info
import org.springdoc.core.models.GroupedOpenApi
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SwaggerConfig {

    @Bean
    fun publicApi(): GroupedOpenApi {
        return GroupedOpenApi.builder()
            .group("public")
            .pathsToMatch("/api/**")
            .build()
    }

    @Bean
    fun apiInfo(): Info {
        return Info()
            .title("Minha API")
            .description("Descrição da minha API")
            .version("1.0.0")
    }
}
