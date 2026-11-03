package co.edu.unbosque.empareja2.dto.response;

import java.time.LocalDateTime;

import co.edu.unbosque.empareja2.entity.TipoReaccion;

/**
 * Una reaccion publica: quien la dio y cuando.
 */
public record ReaccionDTO(Long idAutor, String nombreAutor, String fotoAutor, TipoReaccion tipoReaccion,
        LocalDateTime fechaHora) {
}
