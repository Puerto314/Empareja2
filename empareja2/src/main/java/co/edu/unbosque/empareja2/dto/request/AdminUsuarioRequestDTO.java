package co.edu.unbosque.empareja2.dto.request;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import co.edu.unbosque.empareja2.entity.Genero;
import co.edu.unbosque.empareja2.entity.Rol;
import co.edu.unbosque.empareja2.entity.TipoDocumento;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
        @NotBlank(message = "El usuario/correo es obligatorio") String correo,
        @Size(min = 8, max = 72, message = "La contrasena debe tener entre 8 y 72 caracteres") String contrasena,
        Rol rol,
        @NotNull(message = "El tipo de documento es obligatorio") TipoDocumento tipoDocumento,
        @NotBlank(message = "El documento es obligatorio")
        @Size(max = 30, message = "El documento es demasiado largo") String documento,
        Genero genero,
        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 100, message = "El nombre es demasiado largo") String nombre,
        @NotBlank(message = "El apellido es obligatorio")
        @Size(max = 100, message = "El apellido es demasiado largo") String apellido,
        @Past(message = "La fecha de nacimiento debe ser en el pasado") LocalDate fechaNacimiento,
        @Size(max = 500, message = "La foto de perfil es demasiado larga") String fotoPerfil,
        Set<@NotBlank(message = "Un interes no puede estar vacio")
        @Size(max = 60, message = "Un interes es demasiado largo") String> intereses,
        List<@Valid EstudioRequestDTO> estudios,
        List<@Valid RedPerfilRequestDTO> redes) {
}
