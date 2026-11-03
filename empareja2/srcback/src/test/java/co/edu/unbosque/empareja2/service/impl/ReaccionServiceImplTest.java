package co.edu.unbosque.empareja2.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import co.edu.unbosque.empareja2.TestData;
import co.edu.unbosque.empareja2.dto.response.MeGustaDTO;
import co.edu.unbosque.empareja2.dto.response.ReaccionAdminDTO;
import co.edu.unbosque.empareja2.dto.response.ReaccionDTO;
import co.edu.unbosque.empareja2.entity.Reaccion;
import co.edu.unbosque.empareja2.entity.TipoReaccion;
import co.edu.unbosque.empareja2.entity.Usuario;
import co.edu.unbosque.empareja2.exception.DuplicateResourceException;
import co.edu.unbosque.empareja2.exception.ResourceNotFoundException;
import co.edu.unbosque.empareja2.mapper.InteraccionMapper;
import co.edu.unbosque.empareja2.repository.ReaccionRepository;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ReaccionServiceImplTest {

    @Mock
    private ReaccionRepository reaccionRepository;
    @Mock
    private UsuarioFinder usuarioFinder;

    private ReaccionServiceImpl service;

    private Usuario ana;
    private Usuario luis;

    @BeforeEach
    void preparar() {
        service = new ReaccionServiceImpl(reaccionRepository, usuarioFinder, new InteraccionMapper());
        ana = TestData.usuario(1L, "ana@mail.com", "Ana", "Garcia");
        luis = TestData.usuario(2L, "luis@mail.com", "Luis", "Perez");
        when(usuarioFinder.porCorreo("ana@mail.com")).thenReturn(ana);
        when(usuarioFinder.estudianteVisible(2L)).thenReturn(luis);
        when(usuarioFinder.estudianteVisible(1L)).thenReturn(ana);
    }

    @Test
    void darMeGustaGuardaLaReaccionConElSiguienteNumeroDelAutor() {
        when(reaccionRepository.buscar(1L, 2L, TipoReaccion.ME_GUSTA)).thenReturn(Optional.empty());
        when(reaccionRepository.maximoNumero(1L)).thenReturn(3);
        when(reaccionRepository.contar(2L, TipoReaccion.ME_GUSTA)).thenReturn(1L);

        MeGustaDTO dto = service.darMeGusta("ana@mail.com", 2L);

        ArgumentCaptor<Reaccion> captor = ArgumentCaptor.forClass(Reaccion.class);
        verify(reaccionRepository).save(captor.capture());
        Reaccion guardada = captor.getValue();
        assertThat(guardada.getAutor().getId()).isEqualTo(1L);
        assertThat(guardada.getNumeroReaccion()).isEqualTo(4);
        assertThat(guardada.getDestinatario()).isSameAs(luis);
        assertThat(guardada.getTipoReaccion()).isEqualTo(TipoReaccion.ME_GUSTA);
        assertThat(dto).isEqualTo(new MeGustaDTO(2L, 1L, true));
    }

    @Test
    void noSePuedeDarMeGustaAUnoMismo() {
        assertThatThrownBy(() -> service.darMeGusta("ana@mail.com", 1L)).isInstanceOf(IllegalArgumentException.class);
        verify(reaccionRepository, never()).save(any());
    }

    @Test
    void noSePuedeDarMeGustaDosVecesAlMismoPerfil() {
        when(reaccionRepository.buscar(1L, 2L, TipoReaccion.ME_GUSTA))
                .thenReturn(Optional.of(new Reaccion(ana, luis, 1, TipoReaccion.ME_GUSTA)));

        assertThatThrownBy(() -> service.darMeGusta("ana@mail.com", 2L))
                .isInstanceOf(DuplicateResourceException.class);
        verify(reaccionRepository, never()).save(any());
    }

    @Test
    void darMeGustaAUnPerfilInexistenteDa404() {
        when(usuarioFinder.estudianteVisible(99L)).thenThrow(new ResourceNotFoundException("No existe"));

        assertThatThrownBy(() -> service.darMeGusta("ana@mail.com", 99L))
                .isInstanceOf(ResourceNotFoundException.class);
        verify(reaccionRepository, never()).save(any());
    }

    @Test
    void quitarMeGustaElimina() {
        Reaccion existente = new Reaccion(ana, luis, 1, TipoReaccion.ME_GUSTA);
        when(reaccionRepository.buscar(1L, 2L, TipoReaccion.ME_GUSTA)).thenReturn(Optional.of(existente));
        when(reaccionRepository.contar(2L, TipoReaccion.ME_GUSTA)).thenReturn(0L);

        MeGustaDTO dto = service.quitarMeGusta("ana@mail.com", 2L);

        verify(reaccionRepository).delete(existente);
        assertThat(dto).isEqualTo(new MeGustaDTO(2L, 0L, false));
    }

    @Test
    void quitarMeGustaQueNoExisteDa404() {
        when(reaccionRepository.buscar(1L, 2L, TipoReaccion.ME_GUSTA)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.quitarMeGusta("ana@mail.com", 2L))
                .isInstanceOf(ResourceNotFoundException.class);
        verify(reaccionRepository, never()).delete(any(Reaccion.class));
    }

    @Test
    void listarMeGustaEsPublicoYMuestraQuienLoDio() {
        Reaccion r = new Reaccion(ana, luis, 1, TipoReaccion.ME_GUSTA);
        r.setFechaHora(LocalDateTime.of(2026, 10, 1, 12, 0));
        when(reaccionRepository.listarPorDestinatario(2L, TipoReaccion.ME_GUSTA)).thenReturn(List.of(r));

        List<ReaccionDTO> lista = service.listarMeGusta(2L);

        assertThat(lista).hasSize(1);
        assertThat(lista.get(0).idAutor()).isEqualTo(1L);
        assertThat(lista.get(0).nombreAutor()).isEqualTo("Ana Garcia");
        assertThat(lista.get(0).fechaHora()).isEqualTo(LocalDateTime.of(2026, 10, 1, 12, 0));
    }

    @Test
    void adminListaTodasLasReaccionesConAutorYDestinatario() {
        when(reaccionRepository.listarTodas())
                .thenReturn(List.of(new Reaccion(ana, luis, 1, TipoReaccion.ME_GUSTA)));

        List<ReaccionAdminDTO> lista = service.listarTodasParaAdmin();

        assertThat(lista).hasSize(1);
        assertThat(lista.get(0).nombreAutor()).isEqualTo("Ana Garcia");
        assertThat(lista.get(0).nombreDestinatario()).isEqualTo("Luis Perez");
        assertThat(lista.get(0).numeroReaccion()).isEqualTo(1);
    }

    @Test
    void adminEliminaUnaReaccionYSiNoExisteDa404() {
        when(reaccionRepository.eliminar(1L, 1)).thenReturn(1);
        when(reaccionRepository.eliminar(1L, 9)).thenReturn(0);

        service.eliminarPorAdmin(1L, 1);
        assertThatThrownBy(() -> service.eliminarPorAdmin(1L, 9)).isInstanceOf(ResourceNotFoundException.class);
    }
}
