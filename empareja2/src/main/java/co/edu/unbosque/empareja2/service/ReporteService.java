package co.edu.unbosque.empareja2.service;

import java.util.List;

import co.edu.unbosque.empareja2.dto.request.ReporteRequestDTO;
import co.edu.unbosque.empareja2.dto.response.ReporteDTO;
import co.edu.unbosque.empareja2.entity.EstadoReporte;

/**
 * Contrato de los reportes de perfiles.
 */
public interface ReporteService {

    ReporteDTO reportar(String correoActual, Long idReportado, ReporteRequestDTO request);

    /** Solo para administradores. */
    List<ReporteDTO> listarTodos();

    /** Solo para administradores: reportes con un estado dado. */
    List<ReporteDTO> listarPorEstado(EstadoReporte estado);

    ReporteDTO obtener(Long id);

    /** Solo para administradores: cambia el estado de moderacion del reporte. */
    ReporteDTO actualizarEstado(Long id, EstadoReporte estado);

    void eliminar(Long id);
}
