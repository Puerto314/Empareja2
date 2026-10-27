package co.edu.unbosque.empareja2.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Contenido de un comentario privado.
 */
public record ComentarioRequestDTO(
        @NotBlank(message = "El comentario no puede estar vacio")
        @Size(max = 1000, message = "El comentario no puede superar 1000 caracteres") String contenido) {
}
