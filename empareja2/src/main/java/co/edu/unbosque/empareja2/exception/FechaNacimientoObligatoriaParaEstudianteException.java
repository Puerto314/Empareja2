package co.edu.unbosque.empareja2.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * Se lanza en el caso de un estudiante sin fecha de nacimiento. Se responde
 * con 400.
 */
public class FechaNacimientoObligatoriaParaEstudianteException extends ResponseStatusException {

    /**
     * Crea la excepcion con su mensaje ya definido.
     */
    public FechaNacimientoObligatoriaParaEstudianteException() {
        super(HttpStatus.BAD_REQUEST, "La fecha de nacimiento es obligatoria para un estudiante");
    }
}
