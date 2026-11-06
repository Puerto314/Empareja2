package co.edu.unbosque.empareja2.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * Se lanza en el caso de un reporte de tipo OTRO sin descripcion. Se responde
 * con 400.
 */
public class ReporteOtroSinDescripcionException extends ResponseStatusException {

    /**
     * Crea la excepcion con su mensaje ya definido.
     */
    public ReporteOtroSinDescripcionException() {
        super(HttpStatus.BAD_REQUEST, "Describe el motivo cuando el tipo de reporte es OTRO");
    }
}
