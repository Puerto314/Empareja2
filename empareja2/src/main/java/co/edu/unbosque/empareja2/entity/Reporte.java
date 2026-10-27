package co.edu.unbosque.empareja2.entity;

import java.time.LocalDateTime;

/**
 * REPORTE(id_reporte, fecha_hora, tipo_reporte, descripcion). Relacion
 * Realizar: un usuario realiza muchos reportes (1:N). El modelo no define el
 * perfil reportado; se agrega la relacion "reportado" hacia USUARIO.
 */
public class Reporte {

    private Long idReporte;
    private Usuario realizador;
    private Usuario reportado;
    private TipoReporte tipoReporte;
    private String descripcion;
    private LocalDateTime fechaHora;

    public Reporte() {
    }

    public Reporte(Usuario realizador, Usuario reportado, TipoReporte tipoReporte, String descripcion) {
        this.realizador = realizador;
        this.reportado = reportado;
        this.tipoReporte = tipoReporte;
        this.descripcion = descripcion;
        this.fechaHora = LocalDateTime.now();
    }

    public Long getIdReporte() {
        return idReporte;
    }

    public void setIdReporte(Long idReporte) {
        this.idReporte = idReporte;
    }

    public Usuario getRealizador() {
        return realizador;
    }

    public void setRealizador(Usuario realizador) {
        this.realizador = realizador;
    }

    public Usuario getReportado() {
        return reportado;
    }

    public void setReportado(Usuario reportado) {
        this.reportado = reportado;
    }

    public TipoReporte getTipoReporte() {
        return tipoReporte;
    }

    public void setTipoReporte(TipoReporte tipoReporte) {
        this.tipoReporte = tipoReporte;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }
}
