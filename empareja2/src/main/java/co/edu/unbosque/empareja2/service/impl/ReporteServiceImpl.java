package co.edu.unbosque.empareja2.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.unbosque.empareja2.dto.request.ReporteRequestDTO;
import co.edu.unbosque.empareja2.dto.response.ReporteDTO;
import co.edu.unbosque.empareja2.entity.Reporte;
import co.edu.unbosque.empareja2.entity.Usuario;
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

        String descripcion = request.descripcion() == null ? null : request.descripcion().trim();
        Reporte guardado = reporteRepository
                .save(new Reporte(realizador, reportado, request.tipoReporte(), descripcion));
        return interaccionMapper.toReporteDTO(guardado);
    }

    @Override
    public List<ReporteDTO> listarTodos() {
        return reporteRepository.listarTodos().stream().map(interaccionMapper::toReporteDTO).toList();
    }
}
