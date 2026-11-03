package co.edu.unbosque.empareja2.dto.response;

import java.time.LocalDateTime;

public record ComentarioDTO(Integer numeroComentario, Long idAutor, String nombreAutor, Long idDestinatario,
        String nombreDestinatario, String contenido, LocalDateTime fechaHora) {
}
