package co.edu.unbosque.empareja2.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Relacion Tener: red social + url_perfil + alias.
 */
public record RedPerfilRequestDTO(
        @NotNull(message = "La red social es obligatoria") Long idRedSocial,
        @NotBlank(message = "La url del perfil es obligatoria")
        @Pattern(regexp = "^https?://\\S+$", message = "La url del perfil debe empezar por http:// o https://")
        @Size(max = 300, message = "La url del perfil es demasiado larga") String urlPerfil,
        @Size(max = 100, message = "El alias es demasiado largo") String alias) {
}
