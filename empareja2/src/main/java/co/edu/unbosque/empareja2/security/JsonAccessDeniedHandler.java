package co.edu.unbosque.empareja2.security;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Responde 403 con el cuerpo JSON de errores cuando el usuario no tiene el rol necesario.
 */
public class JsonAccessDeniedHandler implements AccessDeniedHandler {

    /**
     * Crea el manejador.
     */
    public JsonAccessDeniedHandler() {
    }

    /**
     * Escribe la respuesta 403.
     *
     * @param request               la peticion
     * @param response              la respuesta
     * @param accessDeniedException la excepcion de acceso denegado
     * @throws IOException si no se puede escribir la respuesta
     */
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
            AccessDeniedException accessDeniedException) throws IOException {
        RespuestaErrorSeguridad.escribir(request, response, HttpStatus.FORBIDDEN,
                "No tienes permiso para realizar esta accion");
    }
}
