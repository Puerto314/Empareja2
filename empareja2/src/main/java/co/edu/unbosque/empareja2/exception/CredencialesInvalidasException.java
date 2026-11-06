package co.edu.unbosque.empareja2.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * Se lanza cuando se intenta iniciar sesion con un correo o una contrasena
 * incorrectos. Se responde con 401.
 */
public class CredencialesInvalidasException extends ResponseStatusException {

    /**
     * Crea la excepcion con su mensaje ya definido.
     */
    public CredencialesInvalidasException() {
        super(HttpStatus.UNAUTHORIZED, "Credenciales invalidas");
    }
}
