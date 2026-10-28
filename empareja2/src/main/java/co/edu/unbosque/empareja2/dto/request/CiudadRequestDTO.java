package co.edu.unbosque.empareja2.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CiudadRequestDTO(
        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 100, message = "El nombre es demasiado largo") String nombre,
        @NotBlank(message = "El departamento es obligatorio")
        @Size(max = 100, message = "El departamento es demasiado largo") String departamento) {
}
