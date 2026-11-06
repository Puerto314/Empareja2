package co.edu.unbosque.empareja2.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * Se lanza en el caso de un administrador que crea o edita un usuario con un
 * correo/usuario que ya existe. Se responde con 409.
 */
public class CorreoUsuarioYaExisteException extends ResponseStatusException {

    /**
     * Crea la excepcion con su mensaje ya definido.
     */
    public CorreoUsuarioYaExisteException() {
        super(HttpStatus.CONFLICT, "Ya existe un usuario con ese correo/usuario");
    }
}
