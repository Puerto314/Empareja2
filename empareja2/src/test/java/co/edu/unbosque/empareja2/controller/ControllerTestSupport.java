package co.edu.unbosque.empareja2.controller;

import static org.assertj.core.api.Assertions.assertThat;

import java.security.Principal;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.server.ResponseStatusException;

import tools.jackson.databind.json.JsonMapper;

/**
 * Utilidades para probar controladores con MockMvc "standalone" (sin levantar
 * Spring): se prueba el mapeo de rutas, la validacion y el manejo de errores.
 * No hay manejador global de errores: cada excepcion del paquete exception
 * extiende ResponseStatusException y trae su propio codigo HTTP y mensaje, asi
 * que aqui solo se montan los controladores.
 */
abstract class ControllerTestSupport {

    protected static final Principal ANA = new UsernamePasswordAuthenticationToken("ana@mail.com", null);

    protected final JsonMapper json = JsonMapper.builder().build();

    protected MockMvc montar(Object... controladores) {
        return MockMvcBuilders.standaloneSetup(controladores).build();
    }

    /**
     * Verifica que la peticion termino lanzando una excepcion de ese tipo.
     *
     * @param tipo la clase de excepcion esperada
     * @return el verificador para usar en andExpect
     */
    protected static ResultMatcher lanza(Class<? extends Exception> tipo) {
        return result -> assertThat(result.getResolvedException()).isInstanceOf(tipo);
    }

    /**
     * Verifica que la peticion termino con una excepcion de respuesta cuyo mensaje
     * es exactamente el esperado.
     *
     * @param mensaje el mensaje que debe llevar la excepcion
     * @return el verificador para usar en andExpect
     */
    protected static ResultMatcher mensaje(String mensaje) {
        return result -> {
            assertThat(result.getResolvedException()).isInstanceOf(ResponseStatusException.class);
            assertThat(((ResponseStatusException) result.getResolvedException()).getReason()).isEqualTo(mensaje);
        };
    }
}
