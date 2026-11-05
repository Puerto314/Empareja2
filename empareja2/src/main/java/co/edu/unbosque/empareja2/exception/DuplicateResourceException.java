package co.edu.unbosque.empareja2.exception;

/**
 * Se lanza cuando se intenta registrar un dato que debe ser unico (por ejemplo
 * un correo o el nombre de una universidad) y ya existe en la base de datos.
 * Se responde con 409.
 */
public class DuplicateResourceException extends RuntimeException {

    /** Version de serializacion de la excepcion. */
    private static final long serialVersionUID = 1L;

    /**
     * Crea la excepcion con el mensaje que se enviara al cliente.
     *
     * @param mensaje el mensaje que explica que dato esta repetido
     */
    public DuplicateResourceException(String mensaje) {
        super(mensaje);
    }
}
