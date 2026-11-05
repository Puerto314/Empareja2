package co.edu.unbosque.empareja2.dto;

import java.time.LocalDateTime;

import co.edu.unbosque.empareja2.entity.EstadoReporte;
import co.edu.unbosque.empareja2.entity.TipoReporte;

/**
 * Un reporte con el nombre de quien reporta y de quien es reportado.
 */
public class ReporteDTO {

    /** El identificador del reporte. */
    private Long idReporte;

    /** El identificador de quien hizo el reporte. */
    private Long idRealizador;

    /** El nombre completo de quien hizo el reporte. */
    private String nombreRealizador;

    /** El identificador del usuario reportado. */
    private Long idReportado;

    /** El nombre completo del usuario reportado. */
    private String nombreReportado;

    /** El motivo del reporte. */
    private TipoReporte tipoReporte;

    /** El estado de moderacion. */
    private EstadoReporte estado;

    /** La descripcion del reporte. */
    private String descripcion;

    /** La fecha y hora del reporte. */
    private LocalDateTime fechaHora;

    /**
     * Crea un objeto ReporteDTO vacio; los datos se asignan con los metodos set.
     */
    public ReporteDTO() {
    }

    /**
     * Crea un objeto ReporteDTO con todos sus datos.
     *
     * @param idReporte el identificador del reporte
     * @param idRealizador el identificador de quien hizo el reporte
     * @param nombreRealizador el nombre completo de quien hizo el reporte
     * @param idReportado el identificador del usuario reportado
     * @param nombreReportado el nombre completo del usuario reportado
     * @param tipoReporte el motivo del reporte
     * @param estado el estado de moderacion
     * @param descripcion la descripcion del reporte
     * @param fechaHora la fecha y hora del reporte
     */
    public ReporteDTO(Long idReporte, Long idRealizador, String nombreRealizador, Long idReportado,
            String nombreReportado, TipoReporte tipoReporte, EstadoReporte estado, String descripcion,
            LocalDateTime fechaHora) {
        this.idReporte = idReporte;
        this.idRealizador = idRealizador;
        this.nombreRealizador = nombreRealizador;
        this.idReportado = idReportado;
        this.nombreReportado = nombreReportado;
        this.tipoReporte = tipoReporte;
        this.estado = estado;
        this.descripcion = descripcion;
        this.fechaHora = fechaHora;
    }

    /**
     * Obtiene el identificador del reporte.
     *
     * @return el identificador del reporte
     */
    public Long getIdReporte() {
        return idReporte;
    }

    /**
     * Establece el identificador del reporte.
     *
     * @param idReporte el identificador del reporte
     */
    public void setIdReporte(Long idReporte) {
        this.idReporte = idReporte;
    }

    /**
     * Obtiene el identificador de quien hizo el reporte.
     *
     * @return el identificador de quien hizo el reporte
     */
    public Long getIdRealizador() {
        return idRealizador;
    }

    /**
     * Establece el identificador de quien hizo el reporte.
     *
     * @param idRealizador el identificador de quien hizo el reporte
     */
    public void setIdRealizador(Long idRealizador) {
        this.idRealizador = idRealizador;
    }

    /**
     * Obtiene el nombre completo de quien hizo el reporte.
     *
     * @return el nombre completo de quien hizo el reporte
     */
    public String getNombreRealizador() {
        return nombreRealizador;
    }

    /**
     * Establece el nombre completo de quien hizo el reporte.
     *
     * @param nombreRealizador el nombre completo de quien hizo el reporte
     */
    public void setNombreRealizador(String nombreRealizador) {
        this.nombreRealizador = nombreRealizador;
    }

    /**
     * Obtiene el identificador del usuario reportado.
     *
     * @return el identificador del usuario reportado
     */
    public Long getIdReportado() {
        return idReportado;
    }

    /**
     * Establece el identificador del usuario reportado.
     *
     * @param idReportado el identificador del usuario reportado
     */
    public void setIdReportado(Long idReportado) {
        this.idReportado = idReportado;
    }

    /**
     * Obtiene el nombre completo del usuario reportado.
     *
     * @return el nombre completo del usuario reportado
     */
    public String getNombreReportado() {
        return nombreReportado;
    }

    /**
     * Establece el nombre completo del usuario reportado.
     *
     * @param nombreReportado el nombre completo del usuario reportado
     */
    public void setNombreReportado(String nombreReportado) {
        this.nombreReportado = nombreReportado;
    }

    /**
     * Obtiene el motivo del reporte.
     *
     * @return el motivo del reporte
     */
    public TipoReporte getTipoReporte() {
        return tipoReporte;
    }

    /**
     * Establece el motivo del reporte.
     *
     * @param tipoReporte el motivo del reporte
     */
    public void setTipoReporte(TipoReporte tipoReporte) {
        this.tipoReporte = tipoReporte;
    }

    /**
     * Obtiene el estado de moderacion.
     *
     * @return el estado de moderacion
     */
    public EstadoReporte getEstado() {
        return estado;
    }

    /**
     * Establece el estado de moderacion.
     *
     * @param estado el estado de moderacion
     */
    public void setEstado(EstadoReporte estado) {
        this.estado = estado;
    }

    /**
     * Obtiene la descripcion del reporte.
     *
     * @return la descripcion del reporte
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Establece la descripcion del reporte.
     *
     * @param descripcion la descripcion del reporte
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * Obtiene la fecha y hora del reporte.
     *
     * @return la fecha y hora del reporte
     */
    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    /**
     * Establece la fecha y hora del reporte.
     *
     * @param fechaHora la fecha y hora del reporte
     */
    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    /**
     * Devuelve una representacion en texto de este objeto para depuracion.
     *
     * @return el objeto como texto
     */
    @Override
    public String toString() {
        return "ReporteDTO [idReporte=" + idReporte
                + ", idRealizador=" + idRealizador
                + ", nombreRealizador=" + nombreRealizador
                + ", idReportado=" + idReportado
                + ", nombreReportado=" + nombreReportado
                + ", tipoReporte=" + tipoReporte
                + ", estado=" + estado
                + ", descripcion=" + descripcion
                + ", fechaHora=" + fechaHora
                + "]";
    }
}
