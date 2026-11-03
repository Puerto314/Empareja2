package co.edu.unbosque.empareja2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

/**
 * Habilita el boton "Authorize" en Swagger UI para probar los endpoints
 * protegidos pegando el token JWT obtenido en /api/auth/login.
 */
@Configuration
public class OpenApiConfig {

    private static final String SCHEME_NAME = "bearerAuth";

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Empareja2 - API")
                        .description("API REST del catalogo de estudiantes universitarios: registro, catalogo "
                                + "publico, me gusta, comentarios privados y reportes")
                        .version("2.0.0"))
                .addSecurityItem(new SecurityRequirement().addList(SCHEME_NAME))
                .components(new Components().addSecuritySchemes(SCHEME_NAME, new SecurityScheme().name(SCHEME_NAME)
                        .type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")));
    }
}
