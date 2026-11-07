package co.edu.unbosque.empareja2.security;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Escribe, con el mismo formato JSON que el manejador global de errores, las
 * respuestas 401 y 403 que Spring Security genera ANTES de llegar a un
 * controlador (token ausente o vencido, rol insuficiente).
 */
final class RespuestaErrorSeguridad {

    private RespuestaErrorSeguridad() {
    }

    /**
     * Escribe la respuesta de error.
     *
     * @param request  la peticion
     * @param response la respuesta donde se escribe
     * @param estado   el codigo HTTP
     * @param mensaje  el mensaje para el usuario
     * @throws IOException si no se puede escribir la respuesta
     */
    static void escribir(HttpServletRequest request, HttpServletResponse response, HttpStatus estado,
            String mensaje) throws IOException {
        String json = "{\"timestamp\":\"" + Instant.now() + "\",\"status\":" + estado.value() + ",\"error\":\""
                + escapar(estado.getReasonPhrase()) + "\",\"message\":\"" + escapar(mensaje) + "\",\"path\":\""
                + escapar(request.getRequestURI()) + "\",\"details\":[]}";
        response.setStatus(estado.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.getWriter().write(json);
    }

    /**
     * Escapa las comillas, barras y saltos de linea para incluir el texto en un JSON.
     *
     * @param texto el texto a escapar
     * @return el texto escapado
     */
    private static String escapar(String texto) {
        if (texto == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (char c : texto.toCharArray()) {
            switch (c) {
            case '"' -> sb.append("\\\"");
            case '\\' -> sb.append("\\\\");
            case '\n' -> sb.append("\\n");
            case '\r' -> sb.append("\\r");
            case '\t' -> sb.append("\\t");
            default -> sb.append(c < 0x20 ? " " : String.valueOf(c));
            }
        }
        return sb.toString();
    }
}
