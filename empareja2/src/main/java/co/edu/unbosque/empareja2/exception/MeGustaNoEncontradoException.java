package co.edu.unbosque.empareja2.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * Se lanza en el caso de quitar un me gusta que el usuario nunca dio. Se
 * responde con 404.
 */
public class MeGustaNoEncontradoException extends ResponseStatusException {

    /**
     * Crea la excepcion con su mensaje ya definido.
     */
    public MeGustaNoEncontradoException() {
        super(HttpStatus.NOT_FOUND, "No le has dado me gusta a este perfil");
    }
}
