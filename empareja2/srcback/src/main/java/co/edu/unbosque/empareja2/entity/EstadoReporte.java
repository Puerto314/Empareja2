package co.edu.unbosque.empareja2.entity;

/**
 * Valores posibles del atributo "estado" de REPORTE: el ciclo de moderacion que
 * sigue un administrador (nuevo -> en revision -> resuelto o descartado).
 */
public enum EstadoReporte {
    PENDIENTE, EN_REVISION, RESUELTO, DESCARTADO;

    /** Un reporte "activo" todavia no fue cerrado por un administrador. */
    public boolean estaActivo() {
        return this == PENDIENTE || this == EN_REVISION;
    }
}
