package co.edu.unbosque.empareja2.dto.response;

import java.time.LocalDateTime;

import co.edu.unbosque.empareja2.entity.EstadoReporte;
import co.edu.unbosque.empareja2.entity.TipoReporte;

public record ReporteDTO(Long idReporte, Long idRealizador, String nombreRealizador, Long idReportado,
        String nombreReportado, TipoReporte tipoReporte, EstadoReporte estado, String descripcion,
        LocalDateTime fechaHora) {
}
