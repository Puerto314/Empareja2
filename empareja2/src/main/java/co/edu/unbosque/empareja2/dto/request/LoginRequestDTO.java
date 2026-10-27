package co.edu.unbosque.empareja2.dto.request;

import jakarta.validation.constraints.NotBlank;

/**
 * Credenciales para iniciar sesion. No se exige formato de correo porque un
 * administrador puede tener un usuario simple (ej: "Puerto314").
 */
public record LoginRequestDTO(
        @NotBlank(message = "El usuario/correo es obligatorio") String correo,
        @NotBlank(message = "La contrasena es obligatoria") String contrasena) {
}
