package co.edu.unbosque.empareja2.exception;

/**
 * Se lanza en el caso de guardar un estudiante sin carrera ni universidad. Se
 * responde con 500.
 */
public class EstudianteSinEstudioException extends IllegalStateException {

    /**
     * Crea la excepcion con su mensaje ya definido.
     */
    public EstudianteSinEstudioException() {
        super("Un estudiante necesita una carrera y una universidad");
    }
}
