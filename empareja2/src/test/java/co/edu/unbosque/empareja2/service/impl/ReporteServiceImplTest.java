package co.edu.unbosque.empareja2.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import co.edu.unbosque.empareja2.TestData;
import co.edu.unbosque.empareja2.dto.request.ReporteRequestDTO;
import co.edu.unbosque.empareja2.dto.response.ReporteDTO;
import co.edu.unbosque.empareja2.entity.Reporte;
import co.edu.unbosque.empareja2.entity.TipoReporte;
import co.edu.unbosque.empareja2.entity.Usuario;
import co.edu.unbosque.empareja2.exception.ResourceNotFoundException;
import co.edu.unbosque.empareja2.mapper.InteraccionMapper;
import co.edu.unbosque.empareja2.repository.ReporteRepository;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ReporteServiceImplTest {

    @Mock
    private ReporteRepository reporteRepository;
    @Mock
    private UsuarioFinder usuarioFinder;

    private ReporteServiceImpl service;

    private Usuario ana;
    private Usuario luis;

    @BeforeEach
    void preparar() {
        service = new ReporteServiceImpl(reporteRepository, usuarioFinder, new InteraccionMapper());
        ana = TestData.usuario(1L, "ana@mail.com", "Ana", "Garcia");
        luis = TestData.usuario(2L, "luis@mail.com", "Luis", "Perez");
        when(usuarioFinder.porCorreo("ana@mail.com")).thenReturn(ana);
        when(usuarioFinder.estudianteVisible(2L)).thenReturn(luis);
        when(usuarioFinder.estudianteVisible(1L)).thenReturn(ana);
        when(reporteRepository.save(any(Reporte.class))).thenAnswer(inv -> {
            Reporte r = inv.getArgument(0);
            r.setIdReporte(10L);
            return r;
        });
    }

    @Test
    void reportarGuardaQuienReportaAQuienYPorQue() {
        ReporteDTO dto = service.reportar("ana@mail.com", 2L,
                new ReporteRequestDTO(TipoReporte.PERFIL_FALSO, "  Usa fotos de otra persona "));

        ArgumentCaptor<Reporte> captor = ArgumentCaptor.forClass(Reporte.class);
        verify(reporteRepository).save(captor.capture());
        Reporte r = captor.getValue();
        assertThat(r.getRealizador()).isSameAs(ana);
        assertThat(r.getReportado()).isSameAs(luis);
        assertThat(r.getTipoReporte()).isEqualTo(TipoReporte.PERFIL_FALSO);
        assertThat(r.getDescripcion()).isEqualTo("Usa fotos de otra persona");

        assertThat(dto.idReporte()).isEqualTo(10L);
        assertThat(dto.idRealizador()).isEqualTo(1L);
        assertThat(dto.idReportado()).isEqualTo(2L);
    }

    @Test
    void laDescripcionEsOpcional() {
        service.reportar("ana@mail.com", 2L, new ReporteRequestDTO(TipoReporte.SPAM, null));

        ArgumentCaptor<Reporte> captor = ArgumentCaptor.forClass(Reporte.class);
        verify(reporteRepository).save(captor.capture());
        assertThat(captor.getValue().getDescripcion()).isNull();
    }

    @Test
    void noSePuedeReportarElPropioPerfil() {
        assertThatThrownBy(() -> service.reportar("ana@mail.com", 1L, new ReporteRequestDTO(TipoReporte.SPAM, null)))
                .isInstanceOf(IllegalArgumentException.class);
        verify(reporteRepository, never()).save(any());
    }

    @Test
    void reportarUnPerfilInexistenteDa404() {
        when(usuarioFinder.estudianteVisible(99L)).thenThrow(new ResourceNotFoundException("No existe"));

        assertThatThrownBy(() -> service.reportar("ana@mail.com", 99L, new ReporteRequestDTO(TipoReporte.ACOSO, "x")))
                .isInstanceOf(ResourceNotFoundException.class);
        verify(reporteRepository, never()).save(any());
    }

    @Test
    void listarTodosMapeaLosReportes() {
        Reporte r = new Reporte(ana, luis, TipoReporte.ACOSO, "insultos");
        r.setIdReporte(5L);
        r.setFechaHora(LocalDateTime.of(2026, 10, 2, 8, 30));
        when(reporteRepository.listarTodos()).thenReturn(List.of(r));

        List<ReporteDTO> lista = service.listarTodos();

        assertThat(lista).hasSize(1);
        assertThat(lista.get(0).nombreReportado()).isEqualTo("Luis Perez");
        assertThat(lista.get(0).tipoReporte()).isEqualTo(TipoReporte.ACOSO);
    }
}
