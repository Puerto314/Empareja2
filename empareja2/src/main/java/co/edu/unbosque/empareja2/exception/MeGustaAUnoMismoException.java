package co.edu.unbosque.empareja2.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * Se lanza en el caso de darse me gusta a uno mismo. Se responde con 400.
 */
public class MeGustaAUnoMismoException extends ResponseStatusException {

    /**
     * Crea la excepcion con su mensaje ya definido.
     */
    public MeGustaAUnoMismoException() {
        super(HttpStatus.BAD_REQUEST, "No puedes darte me gusta a ti mismo");
    }
}
