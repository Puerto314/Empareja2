package co.edu.unbosque.empareja2.entity;

/**
 * Valores posibles del atributo "tipo_reporte" de REPORTE.
 */
public enum TipoReporte {

    /** Publicidad o mensajes repetitivos no deseados. */
    SPAM,

    /** Hostigamiento hacia otro usuario. */
    ACOSO,

    /** Perfil que se hace pasar por otra persona o con datos falsos. */
    PERFIL_FALSO,

    /** Contenido que no es apropiado para el catalogo. */
    CONTENIDO_INAPROPIADO,

    /** Otro motivo; exige una descripcion. */
    OTRO
}
