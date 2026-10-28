package co.edu.unbosque.empareja2.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Datos editables de una oferta academica: la carrera y la universidad forman la
 * llave y no cambian; solo se corrige la facultad.
 */
public record ActualizarOfertaRequestDTO(
        @NotBlank(message = "La facultad es obligatoria")
        @Size(max = 150, message = "La facultad es demasiado larga") String facultad) {
}
