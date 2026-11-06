package co.edu.unbosque.empareja2.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * Se lanza en el caso de dar me gusta a un perfil que ya tiene el me gusta del
 * usuario. Se responde con 409.
 */
public class MeGustaYaDadoException extends ResponseStatusException {

    /**
     * Crea la excepcion con su mensaje ya definido.
     */
    public MeGustaYaDadoException() {
        super(HttpStatus.CONFLICT, "Ya le diste me gusta a este perfil");
    }
}
