package co.edu.unbosque.empareja2.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.unbosque.empareja2.dto.request.ReporteRequestDTO;
import co.edu.unbosque.empareja2.dto.response.ReporteDTO;
import co.edu.unbosque.empareja2.entity.EstadoReporte;
import co.edu.unbosque.empareja2.entity.Reporte;
import co.edu.unbosque.empareja2.entity.TipoReporte;
import co.edu.unbosque.empareja2.entity.Usuario;
import co.edu.unbosque.empareja2.exception.DuplicateResourceException;
import co.edu.unbosque.empareja2.exception.ResourceNotFoundException;
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

    private final ReporteRepository reporteRepository;
    private final UsuarioFinder usuarioFinder;
    private final InteraccionMapper interaccionMapper;

    public ReporteServiceImpl(ReporteRepository reporteRepository, UsuarioFinder usuarioFinder,
            InteraccionMapper interaccionMapper) {
        this.reporteRepository = reporteRepository;
        this.usuarioFinder = usuarioFinder;
        this.interaccionMapper = interaccionMapper;
    }

    @Override
    @Transactional
    public ReporteDTO reportar(String correoActual, Long idReportado, ReporteRequestDTO request) {
        Usuario realizador = usuarioFinder.porCorreo(correoActual);
        Usuario reportado = usuarioFinder.estudianteVisible(idReportado);

        if (realizador.getId().equals(reportado.getId())) {
            throw new IllegalArgumentException("No puedes reportar tu propio perfil");
        }

        String descripcion = request.descripcion() == null || request.descripcion().isBlank() ? null
                : request.descripcion().trim();
        if (request.tipoReporte() == TipoReporte.OTRO && descripcion == null) {
            throw new IllegalArgumentException("Describe el motivo cuando el tipo de reporte es OTRO");
        }
        if (reporteRepository.existeActivo(realizador.getId(), reportado.getId(), request.tipoReporte())) {
            throw new DuplicateResourceException(
                    "Ya reportaste este perfil por ese motivo y el reporte todavia esta en revision");
        }
        Reporte guardado = reporteRepository
                .save(new Reporte(realizador, reportado, request.tipoReporte(), descripcion));
        return interaccionMapper.toReporteDTO(guardado);
    }

    @Override
    public List<ReporteDTO> listarTodos() {
        return reporteRepository.listarTodos().stream().map(interaccionMapper::toReporteDTO).toList();
    }

    @Override
    public List<ReporteDTO> listarPorEstado(EstadoReporte estado) {
        return reporteRepository.listarPorEstado(estado).stream().map(interaccionMapper::toReporteDTO).toList();
    }

    @Override
    public ReporteDTO obtener(Long id) {
        return interaccionMapper.toReporteDTO(buscar(id));
    }

    @Override
    @Transactional
    public ReporteDTO actualizarEstado(Long id, EstadoReporte estado) {
        Reporte reporte = buscar(id);
        reporteRepository.actualizarEstado(id, estado);
        reporte.setEstado(estado);
        return interaccionMapper.toReporteDTO(reporte);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        buscar(id);
        reporteRepository.deleteById(id);
    }

    private Reporte buscar(Long id) {
        return reporteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No existe el reporte con id " + id));
    }
}
