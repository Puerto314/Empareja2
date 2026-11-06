package co.edu.unbosque.empareja2.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * Se lanza en el caso de crear un usuario sin contrasena. Se responde con 400.
 */
public class ContrasenaObligatoriaAlCrearException extends ResponseStatusException {

    /**
     * Crea la excepcion con su mensaje ya definido.
     */
    public ContrasenaObligatoriaAlCrearException() {
        super(HttpStatus.BAD_REQUEST, "La contrasena es obligatoria al crear un usuario");
    }
}
