package co.edu.unbosque.empareja2.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * Relacion Estudiar: carrera + universidad + semestre.
 */
public record EstudioRequestDTO(
        @NotNull(message = "La carrera es obligatoria") Long idCarrera,
        @NotNull(message = "La universidad es obligatoria") Long idUniversidad,
        @NotNull(message = "El semestre es obligatorio")
        @Min(value = 1, message = "El semestre debe ser mayor o igual a 1")
        @Max(value = 20, message = "El semestre no es valido") Integer semestre) {
}
