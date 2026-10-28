package co.edu.unbosque.empareja2.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;

public record RedSocialRequestDTO(
        @NotBlank(message = "El nombre de la red social es obligatorio")
        @Size(max = 50, message = "El nombre de la red social es demasiado largo") String nombreRedSocial,
        @NotBlank(message = "La url es obligatoria")
        @Size(max = 300, message = "La url es demasiado larga")
        @Pattern(regexp = "^https?://\\S+$", message = "La url debe empezar por http:// o https://") String url) {
}
