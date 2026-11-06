package co.edu.unbosque.empareja2.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * Se lanza en el caso de indicar mas de una carrera con universidad. Se
 * responde con 400.
 */
public class SoloUnaCarreraException extends ResponseStatusException {

    /**
     * Crea la excepcion con su mensaje ya definido.
     */
    public SoloUnaCarreraException() {
        super(HttpStatus.BAD_REQUEST, "Solo puedes indicar una carrera y universidad");
    }
}
