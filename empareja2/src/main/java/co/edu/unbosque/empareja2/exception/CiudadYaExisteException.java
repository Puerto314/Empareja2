package co.edu.unbosque.empareja2.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * Se lanza en el caso de una ciudad repetida en el mismo departamento. Se
 * responde con 409.
 */
public class CiudadYaExisteException extends ResponseStatusException {

    /**
     * Crea la excepcion con su mensaje ya definido.
     */
    public CiudadYaExisteException() {
        super(HttpStatus.CONFLICT, "Ya existe esa ciudad en ese departamento");
    }
}
