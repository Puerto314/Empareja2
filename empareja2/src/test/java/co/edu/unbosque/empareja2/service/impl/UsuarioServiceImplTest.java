package co.edu.unbosque.empareja2.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.crypto.password.PasswordEncoder;

import co.edu.unbosque.empareja2.TestData;
import co.edu.unbosque.empareja2.dto.request.ActualizarPerfilRequestDTO;
import co.edu.unbosque.empareja2.dto.request.AdminUsuarioRequestDTO;
import co.edu.unbosque.empareja2.dto.request.EstudioRequestDTO;
import co.edu.unbosque.empareja2.dto.response.UsuarioPerfilDTO;
import co.edu.unbosque.empareja2.dto.response.UsuarioPublicoDTO;
import co.edu.unbosque.empareja2.entity.Genero;
import co.edu.unbosque.empareja2.entity.Rol;
import co.edu.unbosque.empareja2.entity.TipoDocumento;
import co.edu.unbosque.empareja2.entity.TipoReaccion;
import co.edu.unbosque.empareja2.entity.Usuario;
import co.edu.unbosque.empareja2.exception.DuplicateResourceException;
import co.edu.unbosque.empareja2.exception.OperationNotAllowedException;
import co.edu.unbosque.empareja2.exception.ResourceNotFoundException;
import co.edu.unbosque.empareja2.mapper.UsuarioMapper;
import co.edu.unbosque.empareja2.repository.ConteoReacciones;
import co.edu.unbosque.empareja2.repository.ReaccionRepository;
import co.edu.unbosque.empareja2.repository.UniversidadRepository;
import co.edu.unbosque.empareja2.repository.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class UsuarioServiceImplTest {

    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private UniversidadRepository universidadRepository;
    @Mock
    private ReaccionRepository reaccionRepository;
    @Mock
    private PerfilAssembler perfilAssembler;
    @Mock
    private UsuarioFinder usuarioFinder;
    @Mock
    private PasswordEncoder passwordEncoder;

    private UsuarioServiceImpl service;

    private Usuario ana;
    private Usuario luis;
    private Usuario marta;

    @BeforeEach
    void preparar() {
        service = new UsuarioServiceImpl(usuarioRepository, universidadRepository, reaccionRepository,
                new UsuarioMapper(), perfilAssembler, usuarioFinder,
                passwordEncoder);

        ana = TestData.usuario(1L, "ana@mail.com", "Ana", "Garcia");
        luis = TestData.usuario(2L, "luis@mail.com", "Luis", "Perez");
        marta = TestData.usuario(3L, "marta@mail.com", "Marta", "Ruiz");

        when(usuarioFinder.porCorreo("ana@mail.com")).thenReturn(ana);
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(inv -> inv.getArgument(0));
        when(passwordEncoder.encode(any())).thenReturn("ENC");
    }

    // ------------------------------------------------------------------
    // Catalogo
    // ------------------------------------------------------------------

    @Test
    void catalogoListaALosDemasSinElUsuarioActualYConMeGusta() {
        when(usuarioRepository.findByRolAndIdNotOrderByNombreAscApellidoAsc(Rol.ESTUDIANTE, 1L))
                .thenReturn(List.of(luis, marta));
        when(reaccionRepository.contarPorDestinatarios(List.of(2L, 3L), TipoReaccion.ME_GUSTA))
                .thenReturn(List.of(new ConteoReacciones(2L, 5L)));
        when(reaccionRepository.destinatariosReaccionados(1L, TipoReaccion.ME_GUSTA, List.of(2L, 3L)))
                .thenReturn(List.of(3L));

        List<UsuarioPublicoDTO> catalogo = service.listarCatalogo("ana@mail.com", null);

        assertThat(catalogo).extracting(UsuarioPublicoDTO::id).containsExactly(2L, 3L);
        assertThat(catalogo.get(0).totalMeGusta()).isEqualTo(5L);
        assertThat(catalogo.get(0).meGustaDelUsuarioActual()).isFalse();
        assertThat(catalogo.get(1).totalMeGusta()).isZero();
        assertThat(catalogo.get(1).meGustaDelUsuarioActual()).isTrue();
        verify(usuarioRepository, never()).buscarPorUniversidad(any(), any(), any());
    }

    @Test
    void catalogoFiltraPorUniversidad() {
        when(universidadRepository.existsById(10L)).thenReturn(true);
        when(usuarioRepository.buscarPorUniversidad(Rol.ESTUDIANTE, 1L, 10L)).thenReturn(List.of(luis));

        List<UsuarioPublicoDTO> catalogo = service.listarCatalogo("ana@mail.com", 10L);

        assertThat(catalogo).extracting(UsuarioPublicoDTO::id).containsExactly(2L);
        verify(usuarioRepository, never()).findByRolAndIdNotOrderByNombreAscApellidoAsc(any(), any());
    }

    @Test
    void catalogoConUniversidadInexistenteDa404() {
        when(universidadRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> service.listarCatalogo("ana@mail.com", 99L))
                .isInstanceOf(ResourceNotFoundException.class).hasMessageContaining("99");
        verify(usuarioRepository, never()).buscarPorUniversidad(any(), any(), any());
    }

    @Test
    void rankingGeneralConservaElOrdenDelRepositorioYAgregaLosConteos() {
        when(usuarioRepository.rankingPorMeGusta(Rol.ESTUDIANTE, TipoReaccion.ME_GUSTA, null, 20))
                .thenReturn(List.of(marta, luis));
        when(reaccionRepository.contarPorDestinatarios(List.of(3L, 2L), TipoReaccion.ME_GUSTA))
                .thenReturn(List.of(new ConteoReacciones(3L, 9L), new ConteoReacciones(2L, 4L)));
        when(reaccionRepository.destinatariosReaccionados(1L, TipoReaccion.ME_GUSTA, List.of(3L, 2L)))
                .thenReturn(List.of(2L));

        List<UsuarioPublicoDTO> ranking = service.rankingMeGusta("ana@mail.com", null, 20);

        assertThat(ranking).extracting(UsuarioPublicoDTO::id).containsExactly(3L, 2L);
        assertThat(ranking).extracting(UsuarioPublicoDTO::totalMeGusta).containsExactly(9L, 4L);
        assertThat(ranking.get(1).meGustaDelUsuarioActual()).isTrue();
        verifyNoInteractions(universidadRepository);
    }

    @Test
    void rankingPorUniversidadFiltraEnElRepositorio() {
        when(universidadRepository.existsById(10L)).thenReturn(true);
        when(usuarioRepository.rankingPorMeGusta(Rol.ESTUDIANTE, TipoReaccion.ME_GUSTA, 10L, 20))
                .thenReturn(List.of(luis));

        List<UsuarioPublicoDTO> ranking = service.rankingMeGusta("ana@mail.com", 10L, 20);

        assertThat(ranking).extracting(UsuarioPublicoDTO::id).containsExactly(2L);
    }

    @Test
    void rankingConUniversidadInexistenteDa404() {
        when(universidadRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> service.rankingMeGusta("ana@mail.com", 99L, 20))
                .isInstanceOf(ResourceNotFoundException.class).hasMessageContaining("99");
        verify(usuarioRepository, never()).rankingPorMeGusta(any(), any(), any(), org.mockito.ArgumentMatchers.anyInt());
    }

    @Test
    void rankingConLimiteFueraDeRangoDa400() {
        assertThatThrownBy(() -> service.rankingMeGusta("ana@mail.com", null, 0))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> service.rankingMeGusta("ana@mail.com", null, 101))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void rankingVacioNoConsultaReacciones() {
        when(usuarioRepository.rankingPorMeGusta(Rol.ESTUDIANTE, TipoReaccion.ME_GUSTA, null, 20))
                .thenReturn(List.of());

        assertThat(service.rankingMeGusta("ana@mail.com", null, 20)).isEmpty();
        verifyNoInteractions(reaccionRepository);
    }

    @Test
    void catalogoVacioNoConsultaReacciones() {
        when(usuarioRepository.findByRolAndIdNotOrderByNombreAscApellidoAsc(Rol.ESTUDIANTE, 1L)).thenReturn(List.of());

        assertThat(service.listarCatalogo("ana@mail.com", null)).isEmpty();
        verifyNoInteractions(reaccionRepository);
    }

    @Test
    void perfilPublicoDeUnEstudianteIncluyeConteoDeMeGusta() {
        when(usuarioFinder.estudianteVisible(2L)).thenReturn(luis);
        when(reaccionRepository.contarPorDestinatarios(anyCollection(), any()))
                .thenReturn(List.of(new ConteoReacciones(2L, 1L)));
        when(reaccionRepository.destinatariosReaccionados(any(), any(), anyCollection())).thenReturn(List.of());

        UsuarioPublicoDTO dto = service.obtenerPerfilPublico("ana@mail.com", 2L);

        assertThat(dto.id()).isEqualTo(2L);
        assertThat(dto.totalMeGusta()).isEqualTo(1L);
        assertThat(dto.meGustaDelUsuarioActual()).isFalse();
    }

    @Test
    void perfilPublicoDeUnaCuentaNoVisibleDa404() {
        when(usuarioFinder.estudianteVisible(50L)).thenThrow(new ResourceNotFoundException("No existe"));

        assertThatThrownBy(() -> service.obtenerPerfilPublico("ana@mail.com", 50L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    // ------------------------------------------------------------------
    // Perfil propio
    // ------------------------------------------------------------------

    @Test
    void perfilPropioIncluyeDatosPrivados() {
        UsuarioPerfilDTO dto = service.obtenerPerfilPropio("ana@mail.com");

        assertThat(dto.correo()).isEqualTo("ana@mail.com");
        assertThat(dto.documento()).isEqualTo("1001");
        assertThat(dto.fechaNacimiento()).isNotNull();
    }

    @Test
    void actualizarPerfilPropioAplicaCamposYColecciones() {
        List<EstudioRequestDTO> estudios = List.of(new EstudioRequestDTO(1L, 10L, 5));
        ActualizarPerfilRequestDTO request = new ActualizarPerfilRequestDTO("Anita", "Gomez", Genero.FEMENINO,
                LocalDate.now().minusYears(25), "foto.png", Set.of("cine"), estudios, List.of());

        UsuarioPerfilDTO dto = service.actualizarPerfilPropio("ana@mail.com", request);

        assertThat(dto.nombre()).isEqualTo("Anita");
        assertThat(dto.apellido()).isEqualTo("Gomez");
        assertThat(dto.genero()).isEqualTo(Genero.FEMENINO);
        assertThat(dto.fotoPerfil()).isEqualTo("foto.png");
        verify(perfilAssembler).validarEdadMinima(request.fechaNacimiento());
        verify(perfilAssembler).aplicarIntereses(ana, request.intereses());
        verify(perfilAssembler).sincronizarEstudios(ana, estudios);
        verify(perfilAssembler).sincronizarRedes(ana, request.redes());
        // el correo y el documento NO se pueden cambiar desde el perfil propio
        assertThat(ana.getCorreo()).isEqualTo("ana@mail.com");
        assertThat(ana.getDocumento()).isEqualTo("1001");
    }

    @Test
    void eliminarCuentaPropiaBorraAlUsuario() {
        service.eliminarCuentaPropia("ana@mail.com");

        verify(usuarioRepository).delete(ana);
    }

    // ------------------------------------------------------------------
    // Administracion
    // ------------------------------------------------------------------

    private AdminUsuarioRequestDTO adminRequest(String correo, String contrasena, Rol rol) {
        return new AdminUsuarioRequestDTO(correo, contrasena, rol, TipoDocumento.CC, "999", null, "Nuevo", "Usuario",
                null, null, null, null, null);
    }

    @Test
    void adminCreaUsuarioConContrasenaCifradaYRol() {
        UsuarioPerfilDTO dto = service.crearPorAdmin(adminRequest("nuevo@mail.com", "secreto123", Rol.ADMIN));

        ArgumentCaptor<Usuario> captor = ArgumentCaptor.forClass(Usuario.class);
        verify(usuarioRepository).save(captor.capture());
        assertThat(captor.getValue().getContrasenaHash()).isEqualTo("ENC");
        assertThat(dto.rol()).isEqualTo(Rol.ADMIN);
        assertThat(dto.correo()).isEqualTo("nuevo@mail.com");
    }

    @Test
    void adminNoPuedeCrearConCorreoODocumentoRepetido() {
        when(usuarioRepository.existsByCorreo("repetido@mail.com")).thenReturn(true);
        assertThatThrownBy(() -> service.crearPorAdmin(adminRequest("repetido@mail.com", "secreto123", null)))
                .isInstanceOf(DuplicateResourceException.class);

        when(usuarioRepository.existsByTipoDocumentoAndDocumento(TipoDocumento.CC, "999")).thenReturn(true);
        assertThatThrownBy(() -> service.crearPorAdmin(adminRequest("otro@mail.com", "secreto123", null)))
                .isInstanceOf(DuplicateResourceException.class);
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void adminDebeIndicarContrasenaAlCrear() {
        assertThatThrownBy(() -> service.crearPorAdmin(adminRequest("nuevo@mail.com", " ", null)))
                .isInstanceOf(IllegalArgumentException.class).hasMessageContaining("contrasena");
        assertThatThrownBy(() -> service.crearPorAdmin(adminRequest("nuevo@mail.com", null, null)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void adminActualizaSinContrasenaConservaElHash() {
        when(usuarioFinder.porId(2L)).thenReturn(luis);

        UsuarioPerfilDTO dto = service.actualizarPorAdmin(2L, adminRequest("luis@mail.com", null, null));

        assertThat(luis.getContrasenaHash()).isEqualTo("hash-2");
        assertThat(dto.nombre()).isEqualTo("Nuevo");
        assertThat(luis.getRol()).isEqualTo(Rol.ESTUDIANTE);
    }

    @Test
    void adminActualizaConContrasenaYRolNuevos() {
        when(usuarioFinder.porId(2L)).thenReturn(luis);

        service.actualizarPorAdmin(2L, adminRequest("luis@mail.com", "nuevaClave1", Rol.ADMIN));

        assertThat(luis.getContrasenaHash()).isEqualTo("ENC");
        assertThat(luis.getRol()).isEqualTo(Rol.ADMIN);
    }

    @Test
    void adminNoPuedeCambiarElCorreoPorUnoYaUsado() {
        when(usuarioFinder.porId(2L)).thenReturn(luis);
        when(usuarioRepository.existsByCorreoAndIdNot("ana@mail.com", 2L)).thenReturn(true);

        assertThatThrownBy(() -> service.actualizarPorAdmin(2L, adminRequest("ana@mail.com", null, null)))
                .isInstanceOf(DuplicateResourceException.class);
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void adminNoPuedeCambiarElDocumentoPorUnoYaUsado() {
        when(usuarioFinder.porId(2L)).thenReturn(luis);
        when(usuarioRepository.existsByTipoDocumentoAndDocumentoAndIdNot(TipoDocumento.CC, "999", 2L))
                .thenReturn(true);

        assertThatThrownBy(() -> service.actualizarPorAdmin(2L, adminRequest("luis@mail.com", null, null)))
                .isInstanceOf(DuplicateResourceException.class).hasMessageContaining("documento");
    }

    @Test
    void adminEliminaUsuarioInexistenteDa404() {
        when(usuarioFinder.porId(77L)).thenThrow(new ResourceNotFoundException("No existe"));

        assertThatThrownBy(() -> service.eliminarPorAdmin(77L)).isInstanceOf(ResourceNotFoundException.class);
        verify(usuarioRepository, never()).delete(any());
    }

    @Test
    void adminEliminaUsuario() {
        when(usuarioFinder.porId(2L)).thenReturn(luis);

        service.eliminarPorAdmin(2L);

        verify(usuarioRepository).delete(luis);
    }

    @Test
    void adminListaPerfilesCompletos() {
        when(usuarioRepository.findAll()).thenReturn(List.of(ana, luis));

        assertThat(service.listarParaAdmin()).extracting(UsuarioPerfilDTO::correo).containsExactly("ana@mail.com",
                "luis@mail.com");
    }

    @Test
    void adminCreaEstudianteValidandoLasMismasReglasQueElRegistro() {
        service.crearPorAdmin(adminRequest("nuevo@mail.com", "secreto123", null));

        verify(perfilAssembler).validarCorreo("nuevo@mail.com");
        verify(perfilAssembler).validarDocumento(TipoDocumento.CC, "999");
        verify(perfilAssembler).exigirDatosDeEstudiante(null, null);
        verify(perfilAssembler).sincronizarEstudios(any(Usuario.class), any());
    }

    @Test
    void adminCreaAdministradorSinColeccionesDeCatalogo() {
        service.crearPorAdmin(adminRequest("root", "secreto123", Rol.ADMIN));

        verify(perfilAssembler, never()).validarCorreo(any());
        verify(perfilAssembler, never()).sincronizarEstudios(any(), any());
        verify(perfilAssembler).validarEdadMinima(null);
    }

    @Test
    void noSePuedeEliminarAlUltimoAdministrador() {
        Usuario admin = TestData.usuario(9L, "root", "Root", "Admin");
        admin.setRol(Rol.ADMIN);
        when(usuarioFinder.porId(9L)).thenReturn(admin);
        when(usuarioRepository.contarPorRol(Rol.ADMIN)).thenReturn(1L);

        assertThatThrownBy(() -> service.eliminarPorAdmin(9L)).isInstanceOf(OperationNotAllowedException.class);
        verify(usuarioRepository, never()).delete(any());
    }

    @Test
    void sePuedeEliminarUnAdministradorSiQuedaOtro() {
        Usuario admin = TestData.usuario(9L, "root", "Root", "Admin");
        admin.setRol(Rol.ADMIN);
        when(usuarioFinder.porId(9L)).thenReturn(admin);
        when(usuarioRepository.contarPorRol(Rol.ADMIN)).thenReturn(2L);

        service.eliminarPorAdmin(9L);

        verify(usuarioRepository).delete(admin);
    }

    @Test
    void noSePuedeQuitarElRolAlUltimoAdministrador() {
        Usuario admin = TestData.usuario(9L, "root", "Root", "Admin");
        admin.setRol(Rol.ADMIN);
        when(usuarioFinder.porId(9L)).thenReturn(admin);
        when(usuarioRepository.contarPorRol(Rol.ADMIN)).thenReturn(1L);

        assertThatThrownBy(() -> service.actualizarPorAdmin(9L, adminRequest("root", null, Rol.ESTUDIANTE)))
                .isInstanceOf(OperationNotAllowedException.class);
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void alConvertirEnAdministradorSeLimpianSusColecciones() {
        luis.getIntereses().add("cine");
        when(usuarioFinder.porId(2L)).thenReturn(luis);

        service.actualizarPorAdmin(2L, adminRequest("luis@mail.com", null, Rol.ADMIN));

        assertThat(luis.getIntereses()).isEmpty();
        assertThat(luis.getEstudios()).isEmpty();
        verify(perfilAssembler, never()).sincronizarEstudios(any(), any());
    }
}
