package co.edu.unbosque.empareja2.dto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Estructura estandar para comunicar errores al cliente.
 */
public class ErrorResponseDTO {

    /** El momento en que ocurrio el error. */
    private LocalDateTime timestamp = LocalDateTime.now();

    /** El codigo de estado HTTP. */
    private int status;

    /** El nombre del estado HTTP (por ejemplo Not Found). */
    private String error;

    /** El mensaje que explica el error. */
    private String message;

    /** La ruta que se estaba consultando. */
    private String path;

    /** El detalle de cada campo invalido (puede ser null). */
    private List<String> details;

    /**
     * Crea un objeto ErrorResponseDTO vacio; los datos se asignan con los metodos set.
     */
    public ErrorResponseDTO() {
    }

    /**
     * Crea un error con el momento actual como fecha.
     *
     * @param status  el codigo de estado HTTP
     * @param error   el nombre del estado HTTP
     * @param message el mensaje que explica el error
     * @param path    la ruta que se estaba consultando
     * @param details el detalle de cada campo invalido (puede ser null)
     */
    public ErrorResponseDTO(int status, String error, String message, String path, List<String> details) {
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
        this.details = details;
    }

    /**
     * Obtiene el momento en que ocurrio el error.
     *
     * @return el momento en que ocurrio el error
     */
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    /**
     * Establece el momento en que ocurrio el error.
     *
     * @param timestamp el momento en que ocurrio el error
     */
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Obtiene el codigo de estado HTTP.
     *
     * @return el codigo de estado HTTP
     */
    public int getStatus() {
        return status;
    }

    /**
     * Establece el codigo de estado HTTP.
     *
     * @param status el codigo de estado HTTP
     */
    public void setStatus(int status) {
        this.status = status;
    }

    /**
     * Obtiene el nombre del estado HTTP (por ejemplo Not Found).
     *
     * @return el nombre del estado HTTP (por ejemplo Not Found)
     */
    public String getError() {
        return error;
    }

    /**
     * Establece el nombre del estado HTTP (por ejemplo Not Found).
     *
     * @param error el nombre del estado HTTP (por ejemplo Not Found)
     */
    public void setError(String error) {
        this.error = error;
    }

    /**
     * Obtiene el mensaje que explica el error.
     *
     * @return el mensaje que explica el error
     */
    public String getMessage() {
        return message;
    }

    /**
     * Establece el mensaje que explica el error.
     *
     * @param message el mensaje que explica el error
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Obtiene la ruta que se estaba consultando.
     *
     * @return la ruta que se estaba consultando
     */
    public String getPath() {
        return path;
    }

    /**
     * Establece la ruta que se estaba consultando.
     *
     * @param path la ruta que se estaba consultando
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * Obtiene el detalle de cada campo invalido (puede ser null).
     *
     * @return el detalle de cada campo invalido (puede ser null)
     */
    public List<String> getDetails() {
        return details;
    }

    /**
     * Establece el detalle de cada campo invalido (puede ser null).
     *
     * @param details el detalle de cada campo invalido (puede ser null)
     */
    public void setDetails(List<String> details) {
        this.details = details;
    }

    /**
     * Devuelve una representacion en texto de este objeto para depuracion.
     *
     * @return el objeto como texto
     */
    @Override
    public String toString() {
        return "ErrorResponseDTO [timestamp=" + timestamp
                + ", status=" + status
                + ", error=" + error
                + ", message=" + message
                + ", path=" + path
                + ", details=" + details
                + "]";
    }
}
