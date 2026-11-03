package co.edu.unbosque.empareja2.exception;

/**
 * Se lanza cuando se intenta registrar un dato que debe ser unico (email o
 * codigo estudiantil) y ya existe en la base de datos.
 */
public class DuplicateResourceException extends RuntimeException {

	public DuplicateResourceException(String message) {
		super(message);
	}
}
