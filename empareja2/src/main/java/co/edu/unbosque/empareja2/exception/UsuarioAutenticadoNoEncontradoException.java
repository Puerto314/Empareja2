package co.edu.unbosque.empareja2.exception;

/**
 * Se lanza en el caso de un usuario que se autentico pero no esta en la base
 * de datos. Se responde con 500.
 */
public class UsuarioAutenticadoNoEncontradoException extends IllegalStateException {

    /**
     * Crea la excepcion con su mensaje ya definido.
     */
    public UsuarioAutenticadoNoEncontradoException() {
        super("Usuario autenticado pero no encontrado en base de datos");
    }
}
