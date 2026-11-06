package co.edu.unbosque.empareja2.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * Se lanza en el caso de repetir una carrera en la lista de estudios. Se
 * responde con 400.
 */
public class CarreraRepetidaException extends ResponseStatusException {

    /**
     * Crea la excepcion con su mensaje ya definido.
     *
     * @param idCarrera el identificador de la carrera repetida
     */
    public CarreraRepetidaException(Long idCarrera) {
        super(HttpStatus.BAD_REQUEST, "La carrera con id " + idCarrera + " esta repetida");
    }
}
