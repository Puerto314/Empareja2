package co.edu.unbosque.empareja2.exception;

/**
 * Se lanza cuando se busca un recurso (ej: un estudiante por id) que no existe.
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
