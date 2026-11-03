package co.edu.unbosque.empareja2.dto.request;

import jakarta.validation.constraints.NotNull;

/**
 * Relacion Ofrecer: una universidad ofrece una carrera dentro de una facultad.
 */
public record OfreceRequestDTO(
        @NotNull(message = "La carrera es obligatoria") Long idCarrera,
        @NotNull(message = "La universidad es obligatoria") Long idUniversidad) {
}
