package co.edu.unbosque.empareja2.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * Se lanza en el caso de un usuario que no existe con ese id. Se responde con
 * 404.
 */
public class UsuarioNoEncontradoPorIdException extends ResponseStatusException {

    /**
     * Crea la excepcion con su mensaje ya definido.
     *
     * @param id el identificador que se busco
     */
    public UsuarioNoEncontradoPorIdException(Long id) {
        super(HttpStatus.NOT_FOUND, "No existe un usuario con id " + id);
    }
}
