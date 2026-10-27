package co.edu.unbosque.empareja2.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CiudadRequestDTO(
        @NotBlank(message = "El nombre es obligatorio") String nombre,
        @NotBlank(message = "El departamento es obligatorio") String departamento) {
}
