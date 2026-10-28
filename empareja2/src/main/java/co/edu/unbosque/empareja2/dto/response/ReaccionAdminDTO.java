package co.edu.unbosque.empareja2.dto.response;

import java.time.LocalDateTime;

import co.edu.unbosque.empareja2.entity.TipoReaccion;

/**
 * Una reaccion vista por un administrador: incluye su llave (idAutor +
 * numeroReaccion) y a quien va dirigida, para poder moderarla.
 */
public record ReaccionAdminDTO(Integer numeroReaccion, Long idAutor, String nombreAutor, Long idDestinatario,
        String nombreDestinatario, TipoReaccion tipoReaccion, LocalDateTime fechaHora) {
}
