package co.edu.unbosque.empareja2.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * Se lanza en el caso de registrarse con un correo que ya existe. Se responde
 * con 409.
 */
public class CorreoYaRegistradoException extends ResponseStatusException {

    /**
     * Crea la excepcion con su mensaje ya definido.
     */
    public CorreoYaRegistradoException() {
        super(HttpStatus.CONFLICT, "Ya existe un usuario registrado con ese correo");
    }
}
