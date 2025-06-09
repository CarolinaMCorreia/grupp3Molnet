package org.campusmolndal.grupp3molnet.configs;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocConfig {

    /**
     * Konfigurerar OpenAPI med säkerhetskrav och grundläggande API-information.
     *
     * @return en instans av OpenAPI som innehåller konfigurerad information.
     */
    @Bean
    public OpenAPI customOpenAPI() {
        // Skapar en säkerhetsschemanamn för JWT
        final String securitySchemeName = "bearerAuth";

        // Konfigurerar OpenAPI-instansen med säkerhetskomponenter och information om API:et
        return new OpenAPI()
                .info(new Info()
                        .title("Husdjursregister")
                        .description("API för hantering av husdjursregister med JWT-autentisering. Root URL:  " +
                                "http://husdjursregister1-env.eba-gzkbcjgw.eu-north-1.elasticbeanstalk.com/auth/login")
                        .version("v1.0"))
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName, new SecurityScheme()
                                .name(securitySchemeName)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")));
    }
}
