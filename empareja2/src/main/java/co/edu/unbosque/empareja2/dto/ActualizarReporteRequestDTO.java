package co.edu.unbosque.empareja2.dto;

import co.edu.unbosque.empareja2.entity.EstadoReporte;
import jakarta.validation.constraints.NotNull;

/**
 * Nuevo estado de un reporte (lo cambia un administrador al moderar).
 */
public class ActualizarReporteRequestDTO {

    /** El nuevo estado del reporte. */
    @NotNull(message = "El estado es obligatorio")
    private EstadoReporte estado;

    /**
     * Crea un objeto ActualizarReporteRequestDTO vacio; los datos se asignan con los metodos set.
     */
    public ActualizarReporteRequestDTO() {
    }

    /**
     * Crea un objeto ActualizarReporteRequestDTO con todos sus datos.
     *
     * @param estado el nuevo estado del reporte
     */
    public ActualizarReporteRequestDTO(EstadoReporte estado) {
        this.estado = estado;
    }

    /**
     * Obtiene el nuevo estado del reporte.
     *
     * @return el nuevo estado del reporte
     */
    public EstadoReporte getEstado() {
        return estado;
    }

    /**
     * Establece el nuevo estado del reporte.
     *
     * @param estado el nuevo estado del reporte
     */
    public void setEstado(EstadoReporte estado) {
        this.estado = estado;
    }

    /**
     * Devuelve una representacion en texto de este objeto para depuracion.
     *
     * @return el objeto como texto
     */
    @Override
    public String toString() {
        return "ActualizarReporteRequestDTO [estado=" + estado
                + "]";
    }
}
