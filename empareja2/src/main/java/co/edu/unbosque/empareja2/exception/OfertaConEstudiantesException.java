package co.edu.unbosque.empareja2.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * Se lanza en el caso de eliminar una oferta que algun estudiante esta
 * cursando. Se responde con 409.
 */
public class OfertaConEstudiantesException extends ResponseStatusException {

    /**
     * Crea la excepcion con su mensaje ya definido.
     */
    public OfertaConEstudiantesException() {
        super(HttpStatus.CONFLICT,
                "No se puede eliminar la oferta: hay usuarios que cursan esa carrera en esa universidad");
    }
}
