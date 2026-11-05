package co.edu.unbosque.empareja2.entity;

/**
 * Valores posibles del atributo "estado" de REPORTE: el ciclo de moderacion que
 * sigue un administrador (nuevo -> en revision -> resuelto o descartado).
 */
public enum EstadoReporte {

    /** El reporte acaba de llegar y nadie lo ha revisado. */
    PENDIENTE,

    /** Un administrador esta revisando el reporte. */
    EN_REVISION,

    /** El reporte se atendio y se tomo una decision. */
    RESUELTO,

    /** El reporte se cerro sin tomar accion. */
    DESCARTADO;

    /**
     * Un reporte "activo" todavia no fue cerrado por un administrador.
     *
     * @return true si el estado es PENDIENTE o EN_REVISION
     */
    public boolean estaActivo() {
        return this == PENDIENTE || this == EN_REVISION;
    }
}
