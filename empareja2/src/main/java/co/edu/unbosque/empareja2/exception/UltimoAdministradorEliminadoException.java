package co.edu.unbosque.empareja2.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * Se lanza en el caso de eliminar al unico administrador. Se responde con 409.
 */
public class UltimoAdministradorEliminadoException extends ResponseStatusException {

    /**
     * Crea la excepcion con su mensaje ya definido.
     */
    public UltimoAdministradorEliminadoException() {
        super(HttpStatus.CONFLICT, "No se puede eliminar al ultimo administrador");
    }
}
