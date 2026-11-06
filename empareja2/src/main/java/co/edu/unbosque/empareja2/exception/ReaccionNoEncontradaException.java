package co.edu.unbosque.empareja2.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * Se lanza en el caso de una reaccion que no existe. Se responde con 404.
 */
public class ReaccionNoEncontradaException extends ResponseStatusException {

    /**
     * Crea la excepcion con su mensaje ya definido.
     *
     * @param idAutor        el identificador del autor de la reaccion
     * @param numeroReaccion el numero de la reaccion
     */
    public ReaccionNoEncontradaException(Long idAutor, Integer numeroReaccion) {
        super(HttpStatus.NOT_FOUND, "No existe la reaccion " + numeroReaccion + " del usuario " + idAutor);
    }
}
