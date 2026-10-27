package co.edu.unbosque.empareja2.dto.request;

import jakarta.validation.constraints.NotBlank;

public record RedSocialRequestDTO(
        @NotBlank(message = "El nombre de la red social es obligatorio") String nombreRedSocial,
        @NotBlank(message = "La url es obligatoria") String url) {
}
