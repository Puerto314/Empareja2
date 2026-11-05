package co.edu.unbosque.empareja2.entity;

import java.time.LocalDateTime;

/**
 * Entidad REPORTE(id_reporte, fecha_hora, tipo_reporte, descripcion). Relacion
 * Realizar: un usuario realiza muchos reportes (1:N). El modelo no define el
 * perfil reportado; se agrega la relacion "reportado" hacia USUARIO. El atributo
 * "estado" lo gestiona el administrador (empieza en PENDIENTE).
 */
public class Reporte {

    /** El identificador del reporte. */
    private Long idReporte;

    /** El usuario que hizo el reporte. */
    private Usuario realizador;

    /** El usuario al que se reporta. */
    private Usuario reportado;

    /** El motivo del reporte. */
    private TipoReporte tipoReporte;

    /** La descripcion opcional del reporte. */
    private String descripcion;

    /** El estado de moderacion del reporte. */
    private EstadoReporte estado = EstadoReporte.PENDIENTE;

    /** La fecha y hora en que se hizo el reporte. */
    private LocalDateTime fechaHora;

    /**
     * Crea un objeto Reporte vacio; los datos se asignan con los metodos set.
     */
    public Reporte() {
    }

    /**
     * Crea un reporte nuevo en estado PENDIENTE; la fecha y hora se asignan con el
     * momento actual.
     *
     * @param realizador  el usuario que hace el reporte
     * @param reportado   el usuario al que se reporta
     * @param tipoReporte el motivo del reporte
     * @param descripcion la descripcion opcional del reporte
     */
    public Reporte(Usuario realizador, Usuario reportado, TipoReporte tipoReporte, String descripcion) {
        this.realizador = realizador;
        this.reportado = reportado;
        this.tipoReporte = tipoReporte;
        this.descripcion = descripcion;
        this.fechaHora = LocalDateTime.now();
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
     * Obtiene el usuario que hizo el reporte.
     *
     * @return el usuario que hizo el reporte
     */
    public Usuario getRealizador() {
        return realizador;
    }

    /**
     * Establece el usuario que hizo el reporte.
     *
     * @param realizador el usuario que hizo el reporte
     */
    public void setRealizador(Usuario realizador) {
        this.realizador = realizador;
    }

    /**
     * Obtiene el usuario al que se reporta.
     *
     * @return el usuario al que se reporta
     */
    public Usuario getReportado() {
        return reportado;
    }

    /**
     * Establece el usuario al que se reporta.
     *
     * @param reportado el usuario al que se reporta
     */
    public void setReportado(Usuario reportado) {
        this.reportado = reportado;
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
     * Obtiene la descripcion opcional del reporte.
     *
     * @return la descripcion opcional del reporte
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Establece la descripcion opcional del reporte.
     *
     * @param descripcion la descripcion opcional del reporte
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * Obtiene el estado de moderacion del reporte.
     *
     * @return el estado de moderacion del reporte
     */
    public EstadoReporte getEstado() {
        return estado;
    }

    /**
     * Establece el estado de moderacion del reporte.
     *
     * @param estado el estado de moderacion del reporte
     */
    public void setEstado(EstadoReporte estado) {
        this.estado = estado;
    }

    /**
     * Obtiene la fecha y hora en que se hizo el reporte.
     *
     * @return la fecha y hora en que se hizo el reporte
     */
    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    /**
     * Establece la fecha y hora en que se hizo el reporte.
     *
     * @param fechaHora la fecha y hora en que se hizo el reporte
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
        return "Reporte [idReporte=" + idReporte
                + ", idRealizador=" + (realizador == null ? null : realizador.getId())
                + ", idReportado=" + (reportado == null ? null : reportado.getId())
                + ", tipoReporte=" + tipoReporte
                + ", descripcion=" + descripcion
                + ", estado=" + estado
                + ", fechaHora=" + fechaHora
                + "]";
    }
}
