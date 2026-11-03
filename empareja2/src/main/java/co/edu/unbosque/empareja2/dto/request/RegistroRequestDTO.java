package co.edu.unbosque.empareja2.dto.request;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import co.edu.unbosque.empareja2.entity.Genero;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

/**
 * Datos para registrarse en el catalogo. Se valida todo antes de llegar a la
 * capa de servicio.
 */
public record RegistroRequestDTO(
        @NotBlank(message = "El correo es obligatorio")
        @Email(message = "El correo no tiene un formato valido")
        @Size(max = 100, message = "El correo es demasiado largo") String correo,
        @NotBlank(message = "La contrasena es obligatoria")
        @Size(min = 8, max = 72, message = "La contrasena debe tener entre 8 y 72 caracteres") String contrasena,
        @NotNull(message = "El genero es obligatorio") Genero genero,
        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 50, message = "El nombre es demasiado largo") String nombre,
        @NotBlank(message = "El apellido es obligatorio")
        @Size(max = 50, message = "El apellido es demasiado largo") String apellido,
        @NotNull(message = "La fecha de nacimiento es obligatoria")
        @Past(message = "La fecha de nacimiento debe ser en el pasado") LocalDate fechaNacimiento,
        @Size(max = 255, message = "La foto de perfil es demasiado larga")
        @Pattern(regexp = "^(https?://\\S+)?$", message = "La foto de perfil debe ser una url http:// o https://") String fotoPerfil,
        Set<@NotBlank(message = "Un interes no puede estar vacio")
        @Size(max = 50, message = "Un interes es demasiado largo") String> intereses,
        @NotEmpty(message = "Debes indicar tu carrera y universidad")
        @Size(max = 1, message = "Solo puedes indicar una carrera y universidad")
        List<@Valid EstudioRequestDTO> estudios,
        List<@Valid RedPerfilRequestDTO> redes) {
}
