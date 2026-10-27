package co.edu.unbosque.empareja2.service;

import java.util.List;

import co.edu.unbosque.empareja2.dto.request.ReporteRequestDTO;
import co.edu.unbosque.empareja2.dto.response.ReporteDTO;

/**
 * Contrato de los reportes de perfiles.
 */
public interface ReporteService {

    ReporteDTO reportar(String correoActual, Long idReportado, ReporteRequestDTO request);

    /** Solo para administradores. */
    List<ReporteDTO> listarTodos();
}
