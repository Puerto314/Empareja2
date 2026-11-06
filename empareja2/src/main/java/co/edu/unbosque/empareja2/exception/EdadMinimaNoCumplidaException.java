package co.edu.unbosque.empareja2.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import co.edu.unbosque.empareja2.entity.Usuario;

/**
 * Se lanza en el caso de registrarse sin cumplir la edad minima. Se responde
 * con 400.
 */
public class EdadMinimaNoCumplidaException extends ResponseStatusException {

    /**
     * Crea la excepcion con su mensaje ya definido.
     */
    public EdadMinimaNoCumplidaException() {
        super(HttpStatus.BAD_REQUEST,
                "Debes tener al menos " + Usuario.EDAD_MINIMA + " anos para registrarte");
    }
}
