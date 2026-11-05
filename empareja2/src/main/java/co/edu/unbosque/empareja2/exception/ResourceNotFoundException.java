package co.edu.unbosque.empareja2.exception;

/**
 * Se lanza cuando se busca un recurso (ej: un estudiante por id) que no existe.
 * Se responde con 404.
 */
public class ResourceNotFoundException extends RuntimeException {

    /** Version de serializacion de la excepcion. */
    private static final long serialVersionUID = 1L;

    /**
     * Crea la excepcion con el mensaje que se enviara al cliente.
     *
     * @param mensaje el mensaje que explica que recurso no existe
     */
    public ResourceNotFoundException(String mensaje) {
        super(mensaje);
    }
}
