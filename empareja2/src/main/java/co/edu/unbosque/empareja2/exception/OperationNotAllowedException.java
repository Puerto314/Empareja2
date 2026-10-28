package co.edu.unbosque.empareja2.exception;

/**
 * Se lanza cuando la operacion es valida pero las reglas del negocio no la
 * permiten en el estado actual (ej: eliminar al ultimo administrador o una
 * universidad que todavia tiene estudiantes). Se responde con 409.
 */
public class OperationNotAllowedException extends RuntimeException {

    public OperationNotAllowedException(String message) {
        super(message);
    }
}
