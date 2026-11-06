package co.edu.unbosque.empareja2.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import co.edu.unbosque.empareja2.TestData;
import co.edu.unbosque.empareja2.dto.EstudioRequestDTO;
import co.edu.unbosque.empareja2.dto.RedPerfilRequestDTO;
import co.edu.unbosque.empareja2.entity.Estudio;
import co.edu.unbosque.empareja2.entity.RedSocial;
import co.edu.unbosque.empareja2.entity.Usuario;
import co.edu.unbosque.empareja2.exception.ResourceNotFoundException;
import co.edu.unbosque.empareja2.repository.CarreraRepository;
import co.edu.unbosque.empareja2.repository.OfreceRepository;
import co.edu.unbosque.empareja2.repository.UniversidadRepository;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class PerfilAssemblerTest {

    @Mock
    private CarreraRepository carreraRepository;
    @Mock
    private UniversidadRepository universidadRepository;
    @Mock
    private OfreceRepository ofreceRepository;

    @InjectMocks
    private PerfilAssembler assembler;

    private Usuario usuario;

    @BeforeEach
    void preparar() {
        usuario = TestData.usuario(1L, "ana@mail.com", "Ana", "Garcia");

        when(carreraRepository.buscarPorId(1L)).thenReturn(Optional.of(TestData.carrera(1L, "Medicina")));
        when(carreraRepository.buscarPorId(2L)).thenReturn(Optional.of(TestData.carrera(2L, "Derecho")));
        when(universidadRepository.buscarPorId(10L))
                .thenReturn(Optional.of(TestData.universidad(10L, "Universidad El Bosque", "UNBOSQUE")));
        when(universidadRepository.buscarPorId(20L))
                .thenReturn(Optional.of(TestData.universidad(20L, "Universidad Nacional", "UNAL")));
        when(ofreceRepository.existePorCarreraYUniversidad(anyLong(), anyLong())).thenReturn(true);
    }

    // ---- edad minima ----

    @Test
    void rechazaMenoresDeEdad() {
        LocalDate diecisiete = LocalDate.now().minusYears(17);
        assertThatThrownBy(() -> assembler.validarEdadMinima(diecisiete)).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("18");
    }

    @Test
    void aceptaExactamenteLaEdadMinimaYFechaNula() {
        assertThatCode(() -> assembler.validarEdadMinima(LocalDate.now().minusYears(18))).doesNotThrowAnyException();
        assertThatCode(() -> assembler.validarEdadMinima(null)).doesNotThrowAnyException();
    }

    // ---- intereses ----

    @Test
    void interesesSeLimpianYSeReemplazan() {
        usuario.getIntereses().add("viejo");

        assembler.aplicarIntereses(usuario, List.of(" musica ", "", "  ", "cine", "musica"));

        assertThat(usuario.getIntereses()).containsExactlyInAnyOrder("musica", "cine");
    }

    @Test
    void interesesNulosVaciaLaColeccion() {
        usuario.getIntereses().add("viejo");
        assembler.aplicarIntereses(usuario, null);
        assertThat(usuario.getIntereses()).isEmpty();
    }

    // ---- estudios ----

    @Test
    void agregaEstudioNuevo() {
        assembler.sincronizarEstudios(usuario, List.of(new EstudioRequestDTO(1L, 10L, 4)));

        assertThat(usuario.getEstudios()).hasSize(1);
        Estudio e = usuario.getEstudios().get(0);
        assertThat(e.getCarrera().getIdCarrera()).isEqualTo(1L);
        assertThat(e.getUniversidad().getIdUniversidad()).isEqualTo(10L);
        assertThat(e.getSemestre()).isEqualTo(4);
    }

    @Test
    void actualizaEnSitioUnEstudioExistente() {
        assembler.sincronizarEstudios(usuario, List.of(new EstudioRequestDTO(1L, 10L, 4)));
        Estudio original = usuario.getEstudios().get(0);

        assembler.sincronizarEstudios(usuario, List.of(new EstudioRequestDTO(1L, 20L, 6)));

        assertThat(usuario.getEstudios()).hasSize(1);
        assertThat(usuario.getEstudios().get(0)).isSameAs(original);
        assertThat(original.getUniversidad().getIdUniversidad()).isEqualTo(20L);
        assertThat(original.getSemestre()).isEqualTo(6);
    }

    @Test
    void cambiarDeCarreraReemplazaElEstudio() {
        assembler.sincronizarEstudios(usuario, List.of(new EstudioRequestDTO(1L, 10L, 4)));

        assembler.sincronizarEstudios(usuario, List.of(new EstudioRequestDTO(2L, 10L, 3)));

        assertThat(usuario.getEstudios()).hasSize(1);
        assertThat(usuario.getEstudios().get(0).getCarrera().getIdCarrera()).isEqualTo(2L);
    }

    @Test
    void rechazaMasDeUnEstudio() {
        List<EstudioRequestDTO> dos = List.of(new EstudioRequestDTO(1L, 10L, 4), new EstudioRequestDTO(2L, 10L, 2));
        assertThatThrownBy(() -> assembler.sincronizarEstudios(usuario, dos))
                .isInstanceOf(IllegalArgumentException.class).hasMessageContaining("una carrera");
        assertThat(usuario.getEstudios()).isEmpty();
    }

    @Test
    void rechazaCarreraInexistente() {
        List<EstudioRequestDTO> lista = List.of(new EstudioRequestDTO(99L, 10L, 4));
        assertThatThrownBy(() -> assembler.sincronizarEstudios(usuario, lista))
                .isInstanceOf(ResourceNotFoundException.class).hasMessageContaining("carrera");
    }

    @Test
    void rechazaUniversidadInexistente() {
        List<EstudioRequestDTO> lista = List.of(new EstudioRequestDTO(1L, 99L, 4));
        assertThatThrownBy(() -> assembler.sincronizarEstudios(usuario, lista))
                .isInstanceOf(ResourceNotFoundException.class).hasMessageContaining("universidad");
    }

    @Test
    void rechazaUniversidadQueNoOfreceLaCarrera() {
        when(ofreceRepository.existePorCarreraYUniversidad(1L, 10L)).thenReturn(false);
        List<EstudioRequestDTO> lista = List.of(new EstudioRequestDTO(1L, 10L, 4));

        assertThatThrownBy(() -> assembler.sincronizarEstudios(usuario, lista))
                .isInstanceOf(IllegalArgumentException.class).hasMessageContaining("no ofrece");
        assertThat(usuario.getEstudios()).isEmpty();
    }

    @Test
    void estudiosNulosVaciaLaColeccion() {
        assembler.sincronizarEstudios(usuario, List.of(new EstudioRequestDTO(1L, 10L, 4)));
        assembler.sincronizarEstudios(usuario, null);
        assertThat(usuario.getEstudios()).isEmpty();
    }

    // ---- redes sociales ----

    @Test
    void reemplazaLasRedesDelUsuario() {
        assembler.sincronizarRedes(usuario, List.of(new RedPerfilRequestDTO("Instagram", "https://instagram.com/ana")));
        assertThat(usuario.getRedes()).hasSize(1);
        assertThat(usuario.getRedes().get(0).getTipoRedSocial()).isEqualTo("Instagram");
        assertThat(usuario.getRedes().get(0).getUrlPerfil()).isEqualTo("https://instagram.com/ana");

        assembler.sincronizarRedes(usuario, List.of(new RedPerfilRequestDTO(" Instagram ", " https://instagram.com/ana2 "),
                new RedPerfilRequestDTO("Facebook", "https://facebook.com/ana.fb")));

        assertThat(usuario.getRedes()).extracting(RedSocial::getTipoRedSocial).containsExactly("Instagram", "Facebook");
        assertThat(usuario.getRedes().get(0).getUrlPerfil()).isEqualTo("https://instagram.com/ana2");
    }

    @Test
    void eliminaRedesQueYaNoVienen() {
        assembler.sincronizarRedes(usuario, List.of(new RedPerfilRequestDTO("Instagram", "https://instagram.com/a"),
                new RedPerfilRequestDTO("Facebook", "https://facebook.com/b")));

        assembler.sincronizarRedes(usuario, List.of(new RedPerfilRequestDTO("Facebook", "https://facebook.com/b")));

        assertThat(usuario.getRedes()).extracting(RedSocial::getTipoRedSocial).containsExactly("Facebook");

        assembler.sincronizarRedes(usuario, null);
        assertThat(usuario.getRedes()).isEmpty();
    }

    @Test
    void rechazaTipoDeRedRepetidoSinDistinguirMayusculas() {
        List<RedPerfilRequestDTO> repetidas = List.of(new RedPerfilRequestDTO("Instagram", "https://instagram.com/a"),
                new RedPerfilRequestDTO("instagram", "https://instagram.com/b"));
        assertThatThrownBy(() -> assembler.sincronizarRedes(usuario, repetidas))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void usaSetDeInteresesSinDuplicados() {
        assembler.aplicarIntereses(usuario, Set.of("a", "b"));
        assertThat(usuario.getIntereses()).hasSize(2);
    }

    // ---- datos de estudiante ----

    @Test
    void validaElFormatoDelCorreo() {
        assertThatCode(() -> assembler.validarCorreo("ana@mail.com")).doesNotThrowAnyException();
        assertThatThrownBy(() -> assembler.validarCorreo("Puerto314")).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> assembler.validarCorreo("a@b")).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> assembler.validarCorreo(null)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void unEstudianteNecesitaFechaDeNacimientoYExactamenteUnEstudio() {
        List<EstudioRequestDTO> estudios = List.of(new EstudioRequestDTO(1L, 10L, 3));

        assertThatCode(() -> assembler.exigirDatosDeEstudiante(LocalDate.now().minusYears(20), estudios))
                .doesNotThrowAnyException();
        assertThatThrownBy(() -> assembler.exigirDatosDeEstudiante(null, estudios))
                .isInstanceOf(IllegalArgumentException.class);
        List<EstudioRequestDTO> dos = List.of(new EstudioRequestDTO(1L, 10L, 3), new EstudioRequestDTO(2L, 10L, 3));
        assertThatThrownBy(() -> assembler.exigirDatosDeEstudiante(LocalDate.now().minusYears(20), dos))
                .isInstanceOf(IllegalArgumentException.class).hasMessageContaining("exactamente una");
        assertThatThrownBy(() -> assembler.exigirDatosDeEstudiante(LocalDate.now().minusYears(20), List.of()))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> assembler.exigirDatosDeEstudiante(LocalDate.now().minusYears(20), null))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
