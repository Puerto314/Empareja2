package co.edu.unbosque.empareja2.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * Se lanza en el caso de repetir una red social en la lista de redes. Se
 * responde con 400.
 */
public class RedSocialRepetidaException extends ResponseStatusException {

    /**
     * Crea la excepcion con su mensaje ya definido.
     *
     * @param tipoRedSocial el tipo de red social repetido
     */
    public RedSocialRepetidaException(String tipoRedSocial) {
        super(HttpStatus.BAD_REQUEST, "El tipo de red social \"" + tipoRedSocial + "\" esta repetido");
    }
}
