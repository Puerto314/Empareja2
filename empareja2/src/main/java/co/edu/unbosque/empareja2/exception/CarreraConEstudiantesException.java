package co.edu.unbosque.empareja2.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * Se lanza en el caso de eliminar una carrera que todavia estudian usuarios.
 * Se responde con 409.
 */
public class CarreraConEstudiantesException extends ResponseStatusException {

    /**
     * Crea la excepcion con su mensaje ya definido.
     */
    public CarreraConEstudiantesException() {
        super(HttpStatus.CONFLICT, "No se puede eliminar la carrera: hay usuarios que la estudian");
    }
}
