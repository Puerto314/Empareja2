package co.edu.unbosque.empareja2.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * Se lanza en el caso de estudiar una carrera que la universidad no ofrece. Se
 * responde con 400.
 */
public class UniversidadNoOfreceCarreraException extends ResponseStatusException {

    /**
     * Crea la excepcion con su mensaje ya definido.
     *
     * @param nombreUniversidad el nombre de la universidad
     * @param nombreCarrera     el nombre de la carrera
     */
    public UniversidadNoOfreceCarreraException(String nombreUniversidad, String nombreCarrera) {
        super(HttpStatus.BAD_REQUEST,
                "La universidad '" + nombreUniversidad + "' no ofrece la carrera '" + nombreCarrera + "'");
    }
}
