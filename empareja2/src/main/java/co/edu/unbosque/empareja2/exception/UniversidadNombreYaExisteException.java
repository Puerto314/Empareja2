package co.edu.unbosque.empareja2.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * Se lanza en el caso de una universidad con un nombre que ya existe. Se
 * responde con 409.
 */
public class UniversidadNombreYaExisteException extends ResponseStatusException {

    /**
     * Crea la excepcion con su mensaje ya definido.
     */
    public UniversidadNombreYaExisteException() {
        super(HttpStatus.CONFLICT, "Ya existe una universidad con ese nombre");
    }
}
