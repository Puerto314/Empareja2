package co.edu.unbosque.empareja2.dto;

import java.util.List;

/**
 * Cuerpo JSON unico con el que el back responde TODOS los errores. El front lo
 * lee en utils/error.util.ts: muestra "message" y, si existen, los "details".
 */
public class ErrorRespuestaDTO {

    /** Momento del error en formato ISO-8601. */
    private final String timestamp;

    /** Codigo HTTP del error. */
    private final int status;

    /** Texto estandar del codigo HTTP (por ejemplo "Bad Request"). */
    private final String error;

    /** Mensaje para mostrarle al usuario. */
    private final String message;

    /** Ruta de la peticion que fallo. */
    private final String path;

    /** Detalles adicionales (por ejemplo, un texto por cada campo invalido). */
    private final List<String> details;

    /**
     * Crea la respuesta de error.
     *
     * @param timestamp momento del error en formato ISO-8601
     * @param status    el codigo HTTP
     * @param error     el texto estandar del codigo HTTP
     * @param message   el mensaje para el usuario
     * @param path      la ruta de la peticion
     * @param details   los detalles adicionales (puede ser vacia)
     */
    public ErrorRespuestaDTO(String timestamp, int status, String error, String message, String path,
            List<String> details) {
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
        this.details = details;
    }

    /**
     * Obtiene el momento del error.
     *
     * @return el momento del error en formato ISO-8601
     */
    public String getTimestamp() {
        return timestamp;
    }

    /**
     * Obtiene el codigo HTTP.
     *
     * @return el codigo HTTP
     */
    public int getStatus() {
        return status;
    }

    /**
     * Obtiene el texto estandar del codigo HTTP.
     *
     * @return el texto estandar del codigo HTTP
     */
    public String getError() {
        return error;
    }

    /**
     * Obtiene el mensaje para el usuario.
     *
     * @return el mensaje para el usuario
     */
    public String getMessage() {
        return message;
    }

    /**
     * Obtiene la ruta de la peticion que fallo.
     *
     * @return la ruta de la peticion
     */
    public String getPath() {
        return path;
    }

    /**
     * Obtiene los detalles adicionales.
     *
     * @return los detalles adicionales
     */
    public List<String> getDetails() {
        return details;
    }

    /**
     * Devuelve una representacion en texto de este objeto para depuracion.
     *
     * @return el objeto como texto
     */
    @Override
    public String toString() {
        return "ErrorRespuestaDTO [status=" + status + ", error=" + error + ", message=" + message
                + ", path=" + path + ", details=" + details + "]";
    }
}
