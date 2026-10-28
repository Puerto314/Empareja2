package co.edu.unbosque.empareja2.dto.request;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import co.edu.unbosque.empareja2.entity.Genero;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

/**
 * Datos que un usuario autenticado puede actualizar de su propio perfil. No
 * incluye correo, documento ni contrasena. Las listas (intereses, estudios,
 * redes) REEMPLAZAN a las actuales.
 */
public record ActualizarPerfilRequestDTO(
        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 100, message = "El nombre es demasiado largo") String nombre,
        @NotBlank(message = "El apellido es obligatorio")
        @Size(max = 100, message = "El apellido es demasiado largo") String apellido,
        Genero genero,
        @NotNull(message = "La fecha de nacimiento es obligatoria")
        @Past(message = "La fecha de nacimiento debe ser en el pasado") LocalDate fechaNacimiento,
        @Size(max = 500, message = "La foto de perfil es demasiado larga")
        @Pattern(regexp = "^(https?://\\S+)?$", message = "La foto de perfil debe ser una url http:// o https://") String fotoPerfil,
        Set<@NotBlank(message = "Un interes no puede estar vacio")
        @Size(max = 60, message = "Un interes es demasiado largo") String> intereses,
        @NotEmpty(message = "Debes indicar al menos una carrera y universidad")
        List<@Valid EstudioRequestDTO> estudios,
        List<@Valid RedPerfilRequestDTO> redes) {
}
