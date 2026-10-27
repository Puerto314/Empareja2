package co.edu.unbosque.empareja2.controller;

import java.security.Principal;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import co.edu.unbosque.empareja2.exception.GlobalExceptionHandler;
import tools.jackson.databind.json.JsonMapper;

/**
 * Utilidades para probar controladores con MockMvc "standalone" (sin levantar
 * Spring): se prueba el mapeo de rutas, la validacion y el manejo de errores.
 */
abstract class ControllerTestSupport {

    protected static final Principal ANA = new UsernamePasswordAuthenticationToken("ana@mail.com", null);

    protected final JsonMapper json = JsonMapper.builder().build();

    protected MockMvc montar(Object... controladores) {
        return MockMvcBuilders.standaloneSetup(controladores).setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }
}
