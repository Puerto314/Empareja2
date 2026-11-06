package co.edu.unbosque.empareja2.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * Se lanza en el caso de una oferta academica que no existe. Se responde con
 * 404.
 */
public class OfertaNoEncontradaException extends ResponseStatusException {

    /**
     * Crea la excepcion con su mensaje ya definido.
     *
     * @param idCarrera     el identificador de la carrera
     * @param idUniversidad el identificador de la universidad
     */
    public OfertaNoEncontradaException(Long idCarrera, Long idUniversidad) {
        super(HttpStatus.NOT_FOUND,
                "La universidad " + idUniversidad + " no ofrece la carrera " + idCarrera);
    }
}
