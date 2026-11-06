package co.edu.unbosque.empareja2.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * Se lanza en el caso de quitarle el rol de administrador al unico
 * administrador. Se responde con 409.
 */
public class UltimoAdministradorSinRolException extends ResponseStatusException {

    /**
     * Crea la excepcion con su mensaje ya definido.
     */
    public UltimoAdministradorSinRolException() {
        super(HttpStatus.CONFLICT, "No se puede quitar el rol al ultimo administrador");
    }
}
