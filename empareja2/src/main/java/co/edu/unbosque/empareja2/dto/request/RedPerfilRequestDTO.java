package co.edu.unbosque.empareja2.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Red social + nombre de usuario del estudiante en ella (tabla red_social del modelo).
 */
public record RedPerfilRequestDTO(
        @NotNull(message = "La red social es obligatoria") Long idRedSocial,
        @NotBlank(message = "El nombre de usuario en la red social es obligatorio")
        @Size(max = 50, message = "El nombre de usuario en la red social es demasiado largo") String nombreUsuario) {
}
