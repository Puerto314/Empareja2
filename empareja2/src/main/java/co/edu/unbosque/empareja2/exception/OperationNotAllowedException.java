package co.edu.unbosque.empareja2.exception;

/**
 * Se lanza cuando la operacion es valida pero las reglas del negocio no la
 * permiten en el estado actual (ej: eliminar al ultimo administrador o una
 * universidad que todavia tiene estudiantes). Se responde con 409.
 */
public class OperationNotAllowedException extends RuntimeException {

    /** Version de serializacion de la excepcion. */
    private static final long serialVersionUID = 1L;

    /**
     * Crea la excepcion con el mensaje que se enviara al cliente.
     *
     * @param mensaje el mensaje que explica por que no se permite la operacion
     */
    public OperationNotAllowedException(String mensaje) {
        super(mensaje);
    }
}
