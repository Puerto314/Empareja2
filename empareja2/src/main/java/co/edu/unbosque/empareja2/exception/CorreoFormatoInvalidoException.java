package co.edu.unbosque.empareja2.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * Se lanza en el caso de un correo con formato invalido. Se responde con 400.
 */
public class CorreoFormatoInvalidoException extends ResponseStatusException {

    /**
     * Crea la excepcion con su mensaje ya definido.
     */
    public CorreoFormatoInvalidoException() {
        super(HttpStatus.BAD_REQUEST, "El correo no tiene un formato valido");
    }
}
