package co.edu.unbosque.empareja2.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * Se lanza en el caso de enviarse un comentario a uno mismo. Se responde con
 * 400.
 */
public class ComentarioAUnoMismoException extends ResponseStatusException {

    /**
     * Crea la excepcion con su mensaje ya definido.
     */
    public ComentarioAUnoMismoException() {
        super(HttpStatus.BAD_REQUEST, "No puedes enviarte comentarios a ti mismo");
    }
}
