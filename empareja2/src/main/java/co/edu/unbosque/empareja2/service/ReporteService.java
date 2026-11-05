package co.edu.unbosque.empareja2.service;

import java.util.List;

import co.edu.unbosque.empareja2.dto.ReporteDTO;
import co.edu.unbosque.empareja2.dto.ReporteRequestDTO;
import co.edu.unbosque.empareja2.entity.EstadoReporte;

/**
 * Contrato de los reportes de perfiles.
 */
public interface ReporteService {

    /**
     * Reporta el perfil de otro usuario.
     *
     * @param correoActual el correo del usuario que reporta
     * @param idReportado  el identificador del usuario reportado
     * @param request      el motivo y la descripcion
     * @return el reporte creado
     */
    ReporteDTO reportar(String correoActual, Long idReportado, ReporteRequestDTO request);

    /**
     * Solo para administradores: todos los reportes.
     *
     * @return los reportes
     */
    List<ReporteDTO> listarTodos();

    /**
     * Solo para administradores: reportes con un estado dado.
     *
     * @param estado el estado buscado
     * @return los reportes en ese estado
     */
    List<ReporteDTO> listarPorEstado(EstadoReporte estado);

    /**
     * Obtiene un reporte por id.
     *
     * @param id el identificador del reporte
     * @return el reporte
     */
    ReporteDTO obtener(Long id);

    /**
     * Solo para administradores: cambia el estado de moderacion del reporte.
     *
     * @param id     el identificador del reporte
     * @param estado el estado nuevo
     * @return el reporte actualizado
     */
    ReporteDTO actualizarEstado(Long id, EstadoReporte estado);

    /**
     * Elimina un reporte.
     *
     * @param id el identificador del reporte
     */
    void eliminar(Long id);
}
