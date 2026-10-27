package co.edu.unbosque.empareja2.mapper;

import org.springframework.stereotype.Component;

import co.edu.unbosque.empareja2.dto.response.ComentarioDTO;
import co.edu.unbosque.empareja2.dto.response.ReaccionDTO;
import co.edu.unbosque.empareja2.dto.response.ReporteDTO;
import co.edu.unbosque.empareja2.entity.Comentario;
import co.edu.unbosque.empareja2.entity.Reaccion;
import co.edu.unbosque.empareja2.entity.Reporte;

/**
 * Traduce comentarios, reacciones y reportes a DTOs.
 */
@Component
public class InteraccionMapper {

    public ComentarioDTO toComentarioDTO(Comentario comentario) {
        return new ComentarioDTO(comentario.getNumeroComentario(), comentario.getAutor().getId(),
                comentario.getAutor().getNombreCompleto(), comentario.getDestinatario().getId(),
                comentario.getDestinatario().getNombreCompleto(), comentario.getContenido(),
                comentario.getFechaHora());
    }

    public ReaccionDTO toReaccionDTO(Reaccion reaccion) {
        return new ReaccionDTO(reaccion.getAutor().getId(), reaccion.getAutor().getNombreCompleto(),
                reaccion.getAutor().getFotoPerfil(), reaccion.getTipoReaccion(), reaccion.getFechaHora());
    }

    public ReporteDTO toReporteDTO(Reporte reporte) {
        return new ReporteDTO(reporte.getIdReporte(), reporte.getRealizador().getId(),
                reporte.getRealizador().getNombreCompleto(), reporte.getReportado().getId(),
                reporte.getReportado().getNombreCompleto(), reporte.getTipoReporte(), reporte.getDescripcion(),
                reporte.getFechaHora());
    }
}
