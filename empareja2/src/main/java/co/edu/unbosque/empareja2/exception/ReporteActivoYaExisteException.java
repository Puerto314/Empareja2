package co.edu.unbosque.empareja2.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * Se lanza en el caso de reportar otra vez un perfil por el mismo motivo
 * mientras el reporte anterior sigue sin cerrar. Se responde con 409.
 */
public class ReporteActivoYaExisteException extends ResponseStatusException {

    /**
     * Crea la excepcion con su mensaje ya definido.
     */
    public ReporteActivoYaExisteException() {
        super(HttpStatus.CONFLICT,
                "Ya reportaste este perfil por ese motivo y el reporte todavia esta en revision");
    }
}
