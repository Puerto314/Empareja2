package co.edu.unbosque.empareja2.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * Se lanza en el caso de eliminar una universidad donde todavia estudian
 * usuarios. Se responde con 409.
 */
public class UniversidadConEstudiantesException extends ResponseStatusException {

    /**
     * Crea la excepcion con su mensaje ya definido.
     */
    public UniversidadConEstudiantesException() {
        super(HttpStatus.CONFLICT,
                "No se puede eliminar la universidad: hay usuarios que estudian en ella");
    }
}
