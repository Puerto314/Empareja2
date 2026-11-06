package co.edu.unbosque.empareja2.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * Se lanza en el caso de un usuario que no existe con ese correo. Se responde
 * con 404.
 */
public class UsuarioNoEncontradoPorCorreoException extends ResponseStatusException {

    /**
     * Crea la excepcion con su mensaje ya definido.
     *
     * @param correo el correo que se busco
     */
    public UsuarioNoEncontradoPorCorreoException(String correo) {
        super(HttpStatus.NOT_FOUND, "No existe un usuario con correo " + correo);
    }
}
