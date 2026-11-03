package co.edu.unbosque.empareja2.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CarreraRequestDTO(@NotBlank(message = "El nombre es obligatorio")
        @Size(max = 100, message = "El nombre es demasiado largo") String nombre,
        @NotBlank(message = "La facultad es obligatoria")
        @Size(max = 100, message = "La facultad es demasiado larga") String facultad) {
}
