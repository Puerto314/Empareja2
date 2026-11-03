package co.edu.unbosque.empareja2.dto.request;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import co.edu.unbosque.empareja2.entity.Genero;
import co.edu.unbosque.empareja2.entity.Rol;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

/**
 * Datos que un administrador envia para crear o editar cualquier usuario
 * (estudiante o administrador). Aqui "correo" funciona como nombre de usuario y
 * no se exige formato de correo.
 *
 * La contrasena es obligatoria al crear; al editar puede omitirse para no
 * modificarla.
 */
public record AdminUsuarioRequestDTO(
        @NotBlank(message = "El usuario/correo es obligatorio")
        @Size(max = 100, message = "El usuario/correo es demasiado largo") String correo,
        @Size(min = 8, max = 72, message = "La contrasena debe tener entre 8 y 72 caracteres") String contrasena,
        Rol rol,
        Genero genero,
        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 50, message = "El nombre es demasiado largo") String nombre,
        @NotBlank(message = "El apellido es obligatorio")
        @Size(max = 50, message = "El apellido es demasiado largo") String apellido,
        @Past(message = "La fecha de nacimiento debe ser en el pasado") LocalDate fechaNacimiento,
        @Size(max = 255, message = "La foto de perfil es demasiado larga")
        @Pattern(regexp = "^(https?://\\S+)?$", message = "La foto de perfil debe ser una url http:// o https://") String fotoPerfil,
        Set<@NotBlank(message = "Un interes no puede estar vacio")
        @Size(max = 50, message = "Un interes es demasiado largo") String> intereses,
        @Size(max = 1, message = "Solo se admite una carrera y universidad por estudiante")
        List<@Valid EstudioRequestDTO> estudios,
        List<@Valid RedPerfilRequestDTO> redes) {
}
