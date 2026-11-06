package co.edu.unbosque.empareja2.exception;

/**
 * Se lanza en el caso de no poder volver a leer un usuario recien guardado. Se
 * responde con 500.
 */
public class UsuarioNoRecargadoException extends IllegalStateException {

    /**
     * Crea la excepcion con su mensaje ya definido.
     */
    public UsuarioNoRecargadoException() {
        super("No se pudo recargar el usuario");
    }
}
