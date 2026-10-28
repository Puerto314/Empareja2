package co.edu.unbosque.empareja2.dto.request;

import co.edu.unbosque.empareja2.entity.EstadoReporte;
import jakarta.validation.constraints.NotNull;

/**
 * Nuevo estado de un reporte (lo cambia un administrador al moderar).
 */
public record ActualizarReporteRequestDTO(@NotNull(message = "El estado es obligatorio") EstadoReporte estado) {
}
