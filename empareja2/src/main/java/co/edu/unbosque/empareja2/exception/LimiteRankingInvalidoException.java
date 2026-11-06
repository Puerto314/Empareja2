package co.edu.unbosque.empareja2.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * Se lanza en el caso de un limite de ranking fuera del rango permitido. Se
 * responde con 400.
 */
public class LimiteRankingInvalidoException extends ResponseStatusException {

    /**
     * Crea la excepcion con su mensaje ya definido.
     */
    public LimiteRankingInvalidoException() {
        super(HttpStatus.BAD_REQUEST, "El limite del ranking debe estar entre 1 y 100");
    }
}
