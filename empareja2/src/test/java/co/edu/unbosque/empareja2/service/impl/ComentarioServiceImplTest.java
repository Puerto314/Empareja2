package co.edu.unbosque.empareja2.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
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
import co.edu.unbosque.empareja2.dto.request.ComentarioRequestDTO;
import co.edu.unbosque.empareja2.dto.response.ComentarioDTO;
import co.edu.unbosque.empareja2.entity.Comentario;
import co.edu.unbosque.empareja2.entity.Usuario;
import co.edu.unbosque.empareja2.exception.ResourceNotFoundException;
import co.edu.unbosque.empareja2.mapper.InteraccionMapper;
import co.edu.unbosque.empareja2.repository.ComentarioRepository;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ComentarioServiceImplTest {

    @Mock
    private ComentarioRepository comentarioRepository;
    @Mock
    private UsuarioFinder usuarioFinder;

    private ComentarioServiceImpl service;

    private Usuario ana;
    private Usuario luis;

    @BeforeEach
    void preparar() {
        service = new ComentarioServiceImpl(comentarioRepository, usuarioFinder, new InteraccionMapper());
        ana = TestData.usuario(1L, "ana@mail.com", "Ana", "Garcia");
        luis = TestData.usuario(2L, "luis@mail.com", "Luis", "Perez");
        when(usuarioFinder.porCorreo("ana@mail.com")).thenReturn(ana);
        when(usuarioFinder.porCorreo("luis@mail.com")).thenReturn(luis);
        when(usuarioFinder.estudianteVisible(2L)).thenReturn(luis);
        when(usuarioFinder.estudianteVisible(1L)).thenReturn(ana);
        when(comentarioRepository.save(any(Comentario.class))).thenAnswer(inv -> inv.getArgument(0));
    }

    @Test
    void enviarGuardaElComentarioConSiguienteNumeroDelAutorYSinEspacios() {
        when(comentarioRepository.maximoNumero(1L)).thenReturn(0);

        ComentarioDTO dto = service.enviar("ana@mail.com", 2L, new ComentarioRequestDTO("  Hola, me gusto tu perfil  "));

        ArgumentCaptor<Comentario> captor = ArgumentCaptor.forClass(Comentario.class);
        verify(comentarioRepository).save(captor.capture());
        Comentario c = captor.getValue();
        assertThat(c.getAutor().getId()).isEqualTo(1L);
        assertThat(c.getNumeroComentario()).isEqualTo(1);
        assertThat(c.getAutor()).isSameAs(ana);
        assertThat(c.getDestinatario()).isSameAs(luis);

        assertThat(dto.contenido()).isEqualTo("Hola, me gusto tu perfil");
        assertThat(dto.idAutor()).isEqualTo(1L);
        assertThat(dto.idDestinatario()).isEqualTo(2L);
        assertThat(dto.nombreAutor()).isEqualTo("Ana Garcia");
    }

    @Test
    void elNumeroContinuaDespuesDelMaximoDelAutor() {
        when(comentarioRepository.maximoNumero(1L)).thenReturn(7);

        ComentarioDTO dto = service.enviar("ana@mail.com", 2L, new ComentarioRequestDTO("otro"));

        assertThat(dto.numeroComentario()).isEqualTo(8);
    }

    @Test
    void noSePuedeComentarUnoMismo() {
        assertThatThrownBy(() -> service.enviar("ana@mail.com", 1L, new ComentarioRequestDTO("hola")))
                .isInstanceOf(IllegalArgumentException.class);
        verify(comentarioRepository, never()).save(any());
    }

    @Test
    void comentarAUnDestinatarioInexistenteDa404() {
        when(usuarioFinder.estudianteVisible(99L)).thenThrow(new ResourceNotFoundException("No existe"));

        assertThatThrownBy(() -> service.enviar("ana@mail.com", 99L, new ComentarioRequestDTO("hola")))
                .isInstanceOf(ResourceNotFoundException.class);
        verify(comentarioRepository, never()).save(any());
    }

    @Test
    void cadaUsuarioSoloConsultaSuPropiaBandejaDeRecibidos() {
        Comentario paraLuis = new Comentario(ana, luis, 1, "hola luis");
        paraLuis.setFechaHora(LocalDateTime.now());
        when(comentarioRepository.listarRecibidos(2L)).thenReturn(List.of(paraLuis));
        when(comentarioRepository.listarRecibidos(1L)).thenReturn(List.of());

        // Luis (el destinatario) lo ve...
        List<ComentarioDTO> deLuis = service.listarRecibidos("luis@mail.com");
        assertThat(deLuis).hasSize(1);
        assertThat(deLuis.get(0).contenido()).isEqualTo("hola luis");

        // ...Ana (otra persona) no ve nada, y el servicio solo consulto por su propio id
        assertThat(service.listarRecibidos("ana@mail.com")).isEmpty();
        verify(comentarioRepository).listarRecibidos(2L);
        verify(comentarioRepository).listarRecibidos(1L);
        verifyNoMoreInteractions(comentarioRepository);
    }

    @Test
    void enviadosSeConsultanPorElIdDelAutor() {
        Comentario enviado = new Comentario(ana, luis, 1, "hola luis");
        enviado.setFechaHora(LocalDateTime.now());
        when(comentarioRepository.listarEnviados(1L)).thenReturn(List.of(enviado));

        assertThat(service.listarEnviados("ana@mail.com")).hasSize(1);
        verify(comentarioRepository).listarEnviados(1L);
    }

    @Test
    void adminListaTodosLosComentarios() {
        when(comentarioRepository.listarTodos())
                .thenReturn(List.of(new Comentario(ana, luis, 1, "hola"), new Comentario(luis, ana, 1, "chao")));

        assertThat(service.listarTodosParaAdmin()).hasSize(2);
    }

    @Test
    void adminEliminaUnComentarioYSiNoExisteDa404() {
        when(comentarioRepository.eliminar(1L, 1)).thenReturn(1);
        when(comentarioRepository.eliminar(1L, 9)).thenReturn(0);

        service.eliminarPorAdmin(1L, 1);
        assertThatThrownBy(() -> service.eliminarPorAdmin(1L, 9)).isInstanceOf(ResourceNotFoundException.class);
    }
}
