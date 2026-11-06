package co.edu.unbosque.empareja2.exception;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * Se lanza cuando Spring Security no encuentra al usuario que intenta iniciar
 * sesion.
 */
public class UsuarioDeSesionNoEncontradoException extends UsernameNotFoundException {

    /**
     * Crea la excepcion con su mensaje ya definido.
     *
     * @param correo el correo que se busco
     */
    public UsuarioDeSesionNoEncontradoException(String correo) {
        super("No existe un usuario con el correo: " + correo);
    }
}
