package co.edu.unbosque.empareja2.dto.request;

import co.edu.unbosque.empareja2.entity.TipoReporte;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Datos para reportar un perfil.
 */
public record ReporteRequestDTO(
        @NotNull(message = "El tipo de reporte es obligatorio") TipoReporte tipoReporte,
        @Size(max = 1000, message = "La descripcion no puede superar 1000 caracteres") String descripcion) {
}
