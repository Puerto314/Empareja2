package co.edu.unbosque.empareja2.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CarreraRequestDTO(@NotBlank(message = "El nombre es obligatorio") String nombre) {
}
