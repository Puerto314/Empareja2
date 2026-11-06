package co.edu.unbosque.empareja2.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * Se lanza en el caso de un estudiante que no indica exactamente una carrera
 * con su universidad. Se responde con 400.
 */
public class DebeIndicarUnaCarreraException extends ResponseStatusException {

    /**
     * Crea la excepcion con su mensaje ya definido.
     */
    public DebeIndicarUnaCarreraException() {
        super(HttpStatus.BAD_REQUEST, "Debes indicar exactamente una carrera y universidad");
    }
}
