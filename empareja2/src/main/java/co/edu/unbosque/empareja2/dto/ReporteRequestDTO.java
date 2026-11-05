package co.edu.unbosque.empareja2.dto;

import co.edu.unbosque.empareja2.entity.TipoReporte;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Datos para reportar un perfil.
 */
public class ReporteRequestDTO {

    /** El motivo del reporte. */
    @NotNull(message = "El tipo de reporte es obligatorio")
    private TipoReporte tipoReporte;

    /** La descripcion del reporte (obligatoria si el tipo es OTRO). */
    @Size(max = 1000, message = "La descripcion no puede superar 1000 caracteres")
    private String descripcion;

    /**
     * Crea un objeto ReporteRequestDTO vacio; los datos se asignan con los metodos set.
     */
    public ReporteRequestDTO() {
    }

    /**
     * Crea un objeto ReporteRequestDTO con todos sus datos.
     *
     * @param tipoReporte el motivo del reporte
     * @param descripcion la descripcion del reporte (obligatoria si el tipo es OTRO)
     */
    public ReporteRequestDTO(TipoReporte tipoReporte, String descripcion) {
        this.tipoReporte = tipoReporte;
        this.descripcion = descripcion;
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
     * Obtiene la descripcion del reporte (obligatoria si el tipo es OTRO).
     *
     * @return la descripcion del reporte (obligatoria si el tipo es OTRO)
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Establece la descripcion del reporte (obligatoria si el tipo es OTRO).
     *
     * @param descripcion la descripcion del reporte (obligatoria si el tipo es OTRO)
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * Devuelve una representacion en texto de este objeto para depuracion.
     *
     * @return el objeto como texto
     */
    @Override
    public String toString() {
        return "ReporteRequestDTO [tipoReporte=" + tipoReporte
                + ", descripcion=" + descripcion
                + "]";
    }
}
