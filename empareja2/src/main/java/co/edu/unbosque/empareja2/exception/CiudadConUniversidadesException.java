package co.edu.unbosque.empareja2.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * Se lanza en el caso de eliminar una ciudad que todavia tiene universidades.
 * Se responde con 409.
 */
public class CiudadConUniversidadesException extends ResponseStatusException {

    /**
     * Crea la excepcion con su mensaje ya definido.
     */
    public CiudadConUniversidadesException() {
        super(HttpStatus.CONFLICT,
                "No se puede eliminar la ciudad: todavia tiene universidades registradas");
    }
}
