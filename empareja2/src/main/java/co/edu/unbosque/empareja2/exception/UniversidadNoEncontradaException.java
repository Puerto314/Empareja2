package co.edu.unbosque.empareja2.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * Se lanza en el caso de una universidad que no existe. Se responde con 404.
 */
public class UniversidadNoEncontradaException extends ResponseStatusException {

    /**
     * Crea la excepcion con su mensaje ya definido.
     *
     * @param id el identificador que se busco
     */
    public UniversidadNoEncontradaException(Long id) {
        super(HttpStatus.NOT_FOUND, "No existe la universidad con id " + id);
    }
}
