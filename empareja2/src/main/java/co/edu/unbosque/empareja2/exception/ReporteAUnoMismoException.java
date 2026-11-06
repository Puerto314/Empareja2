package co.edu.unbosque.empareja2.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * Se lanza en el caso de reportar el propio perfil. Se responde con 400.
 */
public class ReporteAUnoMismoException extends ResponseStatusException {

    /**
     * Crea la excepcion con su mensaje ya definido.
     */
    public ReporteAUnoMismoException() {
        super(HttpStatus.BAD_REQUEST, "No puedes reportar tu propio perfil");
    }
}
