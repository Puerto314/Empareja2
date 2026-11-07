package co.edu.unbosque.empareja2.security;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Responde 401 con el cuerpo JSON de errores cuando falta el token o no es valido.
 */
public class JsonAuthenticationEntryPoint implements AuthenticationEntryPoint {

    /**
     * Crea el punto de entrada.
     */
    public JsonAuthenticationEntryPoint() {
    }

    /**
     * Escribe la respuesta 401.
     *
     * @param request       la peticion
     * @param response      la respuesta
     * @param authException la excepcion de autenticacion
     * @throws IOException si no se puede escribir la respuesta
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException {
        RespuestaErrorSeguridad.escribir(request, response, HttpStatus.UNAUTHORIZED,
                "Tu sesion no es valida o expiro. Inicia sesion de nuevo");
    }
}
