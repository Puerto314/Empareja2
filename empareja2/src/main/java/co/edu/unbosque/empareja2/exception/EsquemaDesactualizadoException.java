package co.edu.unbosque.empareja2.exception;

/**
 * Se lanza en el caso de arrancar con una base de datos que todavia no tiene
 * los nombres de atributos nuevos.
 */
public class EsquemaDesactualizadoException extends IllegalStateException {

    /**
     * Crea la excepcion con su mensaje ya definido.
     */
    public EsquemaDesactualizadoException() {
        super("La base de datos todavia tiene el esquema anterior " +
                "(faltan usuario.nombre_usuario y/o red_social.id_usuario). " +
                "Ejecuta src/main/resources/db/migracion_nombres_atributos.sql una sola vez y vuelve a arrancar.");
    }
}
