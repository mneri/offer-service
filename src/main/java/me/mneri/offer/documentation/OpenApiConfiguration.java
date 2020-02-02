package me.mneri.offer.documentation;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger and Open API configuration.
 *
 * @author mneri
 */
@Configuration
public class OpenApiConfiguration {
    /**
     * Return an application wide {@link OpenAPI} instance.
     *
     * @return The {@link OpenAPI} instance.
     */
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(new Info()
                        .title("offer-service API")
                        .description("Sample SpringBoot RESTful service."));

    }
}