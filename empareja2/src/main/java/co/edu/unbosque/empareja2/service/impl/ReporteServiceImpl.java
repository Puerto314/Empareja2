package co.edu.unbosque.empareja2.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.unbosque.empareja2.dto.ReporteDTO;
import co.edu.unbosque.empareja2.dto.ReporteRequestDTO;
import co.edu.unbosque.empareja2.entity.EstadoReporte;
import co.edu.unbosque.empareja2.entity.Reporte;
import co.edu.unbosque.empareja2.entity.TipoReporte;
import co.edu.unbosque.empareja2.entity.Usuario;
import co.edu.unbosque.empareja2.exception.LanzadorException;
import co.edu.unbosque.empareja2.mapper.InteraccionMapper;
import co.edu.unbosque.empareja2.repository.ReporteRepository;
import co.edu.unbosque.empareja2.service.ReporteService;

/**
 * Reportes de perfiles hechos por los usuarios; solo un administrador los
 * puede consultar.
 */
@Service
@Transactional(readOnly = true)
public class ReporteServiceImpl implements ReporteService {

    /** Acceso a los datos de los reportes. */
    private final ReporteRepository reporteRepository;

    /** Busca usuarios con el error 404 si no existen. */
    private final UsuarioFinder usuarioFinder;

    /** Convierte reportes en DTOs. */
    private final InteraccionMapper interaccionMapper;

    /**
     * Crea el servicio.
     *
     * @param reporteRepository el repositorio de reportes
     * @param usuarioFinder     el buscador de usuarios
     * @param interaccionMapper el convertidor de reportes
     */
    public ReporteServiceImpl(ReporteRepository reporteRepository, UsuarioFinder usuarioFinder,
            InteraccionMapper interaccionMapper) {
        this.reporteRepository = reporteRepository;
        this.usuarioFinder = usuarioFinder;
        this.interaccionMapper = interaccionMapper;
    }

    /**
     * Reporta el perfil de otro estudiante (no el propio). Si el tipo es OTRO la
     * descripcion es obligatoria, y no se admite un reporte igual que siga sin
     * cerrar.
     *
     * @param correoActual el correo del usuario que reporta
     * @param idReportado  el identificador del usuario reportado
     * @param request      el motivo y la descripcion
     * @return el reporte creado
     */
    @Override
    @Transactional
    public ReporteDTO reportar(String correoActual, Long idReportado, ReporteRequestDTO request) {
        Usuario realizador = usuarioFinder.porCorreo(correoActual);
        Usuario reportado = usuarioFinder.estudianteVisible(idReportado);

        if (realizador.getId().equals(reportado.getId())) {
            LanzadorException.reporteAUnoMismo();
        }

        String descripcion = request.getDescripcion() == null || request.getDescripcion().isBlank() ? null
                : request.getDescripcion().trim();
        if (request.getTipoReporte() == TipoReporte.OTRO && descripcion == null) {
            LanzadorException.reporteOtroSinDescripcion();
        }
        if (reporteRepository.existeActivo(realizador.getId(), reportado.getId(), request.getTipoReporte())) {
            LanzadorException.reporteActivoYaExiste();
        }
        Reporte guardado = reporteRepository
                .guardar(new Reporte(realizador, reportado, request.getTipoReporte(), descripcion));
        return interaccionMapper.toReporteDTO(guardado);
    }

    /**
     * Solo para administradores: todos los reportes.
     *
     * @return los reportes
     */
    @Override
    public List<ReporteDTO> listarTodos() {
        return reporteRepository.listarTodos().stream().map(interaccionMapper::toReporteDTO).toList();
    }

    /**
     * Solo para administradores: reportes con un estado dado.
     *
     * @param estado el estado buscado
     * @return los reportes en ese estado
     */
    @Override
    public List<ReporteDTO> listarPorEstado(EstadoReporte estado) {
        return reporteRepository.listarPorEstado(estado).stream().map(interaccionMapper::toReporteDTO).toList();
    }

    /**
     * Obtiene un reporte por id.
     *
     * @param id el identificador del reporte
     * @return el reporte
     */
    @Override
    public ReporteDTO obtener(Long id) {
        return interaccionMapper.toReporteDTO(buscar(id));
    }

    /**
     * Solo para administradores: cambia el estado de moderacion del reporte.
     *
     * @param id     el identificador del reporte
     * @param estado el estado nuevo
     * @return el reporte actualizado
     */
    @Override
    @Transactional
    public ReporteDTO actualizarEstado(Long id, EstadoReporte estado) {
        Reporte reporte = buscar(id);
        reporteRepository.actualizarEstado(id, estado);
        reporte.setEstado(estado);
        return interaccionMapper.toReporteDTO(reporte);
    }

    /**
     * Elimina un reporte.
     *
     * @param id el identificador del reporte
     */
    @Override
    @Transactional
    public void eliminar(Long id) {
        buscar(id);
        reporteRepository.eliminar(id);
    }

    /**
     * Busca un reporte y lanza 404 si no existe.
     *
     * @param id el identificador del reporte
     * @return el reporte
     */
    private Reporte buscar(Long id) {
        Reporte reporte = reporteRepository.buscarPorId(id).orElse(null);
        if (reporte == null) {
            LanzadorException.reporteNoEncontrado(id);
        }
        return reporte;
    }
}
