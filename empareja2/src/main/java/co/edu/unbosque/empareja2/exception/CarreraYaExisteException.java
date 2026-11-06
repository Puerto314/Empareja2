package co.edu.unbosque.empareja2.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * Se lanza en el caso de una carrera con un nombre que ya existe. Se responde
 * con 409.
 */
public class CarreraYaExisteException extends ResponseStatusException {

    /**
     * Crea la excepcion con su mensaje ya definido.
     */
    public CarreraYaExisteException() {
        super(HttpStatus.CONFLICT, "Ya existe una carrera con ese nombre");
    }
}
