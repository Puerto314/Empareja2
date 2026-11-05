package co.edu.unbosque.empareja2.mapper;

import org.springframework.stereotype.Component;

import co.edu.unbosque.empareja2.dto.ComentarioDTO;
import co.edu.unbosque.empareja2.dto.ReaccionAdminDTO;
import co.edu.unbosque.empareja2.dto.ReaccionDTO;
import co.edu.unbosque.empareja2.dto.ReporteDTO;
import co.edu.unbosque.empareja2.entity.Comentario;
import co.edu.unbosque.empareja2.entity.Reaccion;
import co.edu.unbosque.empareja2.entity.Reporte;

/**
 * Traduce comentarios, reacciones y reportes a DTOs.
 */
@Component
public class InteraccionMapper {

    /**
     * Convierte un comentario en su DTO.
     *
     * @param comentario el comentario
     * @return el DTO del comentario
     */
    public ComentarioDTO toComentarioDTO(Comentario comentario) {
        return new ComentarioDTO(comentario.getNumeroComentario(), comentario.getAutor().getId(),
                comentario.getAutor().getNombreCompleto(), comentario.getDestinatario().getId(),
                comentario.getDestinatario().getNombreCompleto(), comentario.getContenido(),
                comentario.getFechaHora());
    }

    /**
     * Convierte una reaccion en su DTO publico (quien la dio y cuando).
     *
     * @param reaccion la reaccion
     * @return el DTO de la reaccion
     */
    public ReaccionDTO toReaccionDTO(Reaccion reaccion) {
        return new ReaccionDTO(reaccion.getAutor().getId(), reaccion.getAutor().getNombreCompleto(),
                reaccion.getAutor().getFotoPerfil(), reaccion.getTipoReaccion(), reaccion.getFechaHora());
    }

    /**
     * Convierte una reaccion en su DTO para administradores (con su llave y su
     * destinatario).
     *
     * @param reaccion la reaccion
     * @return el DTO de la reaccion para administradores
     */
    public ReaccionAdminDTO toReaccionAdminDTO(Reaccion reaccion) {
        return new ReaccionAdminDTO(reaccion.getNumeroReaccion(), reaccion.getAutor().getId(),
                reaccion.getAutor().getNombreCompleto(), reaccion.getDestinatario().getId(),
                reaccion.getDestinatario().getNombreCompleto(), reaccion.getTipoReaccion(), reaccion.getFechaHora());
    }

    /**
     * Convierte un reporte en su DTO.
     *
     * @param reporte el reporte
     * @return el DTO del reporte
     */
    public ReporteDTO toReporteDTO(Reporte reporte) {
        return new ReporteDTO(reporte.getIdReporte(), reporte.getRealizador().getId(),
                reporte.getRealizador().getNombreCompleto(), reporte.getReportado().getId(),
                reporte.getReportado().getNombreCompleto(), reporte.getTipoReporte(), reporte.getEstado(),
                reporte.getDescripcion(), reporte.getFechaHora());
    }
}
