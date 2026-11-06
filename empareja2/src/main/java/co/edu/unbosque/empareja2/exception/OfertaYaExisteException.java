package co.edu.unbosque.empareja2.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * Se lanza en el caso de una oferta que ya estaba registrada. Se responde con
 * 409.
 */
public class OfertaYaExisteException extends ResponseStatusException {

    /**
     * Crea la excepcion con su mensaje ya definido.
     */
    public OfertaYaExisteException() {
        super(HttpStatus.CONFLICT, "Esa universidad ya ofrece esa carrera");
    }
}
