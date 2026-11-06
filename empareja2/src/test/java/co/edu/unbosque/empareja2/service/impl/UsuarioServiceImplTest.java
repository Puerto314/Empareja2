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
import co.edu.unbosque.empareja2.dto.ActualizarPerfilRequestDTO;
import co.edu.unbosque.empareja2.dto.AdminUsuarioRequestDTO;
import co.edu.unbosque.empareja2.dto.ConteoReaccionesDTO;
import co.edu.unbosque.empareja2.dto.EstudioRequestDTO;
import co.edu.unbosque.empareja2.dto.UsuarioPerfilDTO;
import co.edu.unbosque.empareja2.dto.UsuarioPublicoDTO;
import co.edu.unbosque.empareja2.entity.Genero;
import co.edu.unbosque.empareja2.entity.Rol;
import co.edu.unbosque.empareja2.entity.Usuario;
import co.edu.unbosque.empareja2.exception.ContrasenaObligatoriaAlCrearException;
import co.edu.unbosque.empareja2.exception.CorreoUsuarioYaExisteException;
import co.edu.unbosque.empareja2.exception.LimiteRankingInvalidoException;
import co.edu.unbosque.empareja2.exception.UltimoAdministradorEliminadoException;
import co.edu.unbosque.empareja2.exception.UltimoAdministradorSinRolException;
import co.edu.unbosque.empareja2.exception.UniversidadNoEncontradaException;
import co.edu.unbosque.empareja2.exception.UsuarioNoEncontradoPorIdException;
import co.edu.unbosque.empareja2.mapper.UsuarioMapper;
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
				new UsuarioMapper(), perfilAssembler, usuarioFinder, passwordEncoder);

		ana = TestData.usuario(1L, "ana@mail.com", "Ana", "Garcia");
		luis = TestData.usuario(2L, "luis@mail.com", "Luis", "Perez");
		marta = TestData.usuario(3L, "marta@mail.com", "Marta", "Ruiz");

		when(usuarioFinder.porCorreo("ana@mail.com")).thenReturn(ana);
		when(usuarioRepository.guardar(any(Usuario.class))).thenAnswer(inv -> inv.getArgument(0));
		when(passwordEncoder.encode(any())).thenReturn("ENC");
	}

	// ------------------------------------------------------------------
	// Catalogo
	// ------------------------------------------------------------------

	@Test
	void catalogoListaALosDemasSinElUsuarioActualYConMeGusta() {
		when(usuarioRepository.listarPorRolExcepto(Rol.ESTUDIANTE, 1L)).thenReturn(List.of(luis, marta));
		when(reaccionRepository.contarPorDestinatarios(List.of(2L, 3L)))
				.thenReturn(List.of(new ConteoReaccionesDTO(2L, 5L)));
		when(reaccionRepository.destinatariosReaccionados(1L, List.of(2L, 3L))).thenReturn(List.of(3L));

		List<UsuarioPublicoDTO> catalogo = service.listarCatalogo("ana@mail.com", null);

		assertThat(catalogo).extracting(UsuarioPublicoDTO::getId).containsExactly(2L, 3L);
		assertThat(catalogo.get(0).getTotalMeGusta()).isEqualTo(5L);
		assertThat(catalogo.get(0).isMeGustaDelUsuarioActual()).isFalse();
		assertThat(catalogo.get(1).getTotalMeGusta()).isZero();
		assertThat(catalogo.get(1).isMeGustaDelUsuarioActual()).isTrue();
		verify(usuarioRepository, never()).listarPorUniversidad(any(), any(), any());
	}

	@Test
	void catalogoFiltraPorUniversidad() {
		when(universidadRepository.existePorId(10L)).thenReturn(true);
		when(usuarioRepository.listarPorUniversidad(Rol.ESTUDIANTE, 1L, 10L)).thenReturn(List.of(luis));

		List<UsuarioPublicoDTO> catalogo = service.listarCatalogo("ana@mail.com", 10L);

		assertThat(catalogo).extracting(UsuarioPublicoDTO::getId).containsExactly(2L);
		verify(usuarioRepository, never()).listarPorRolExcepto(any(), any());
	}

	@Test
	void catalogoConUniversidadInexistenteDa404() {
		when(universidadRepository.existePorId(99L)).thenReturn(false);

		assertThatThrownBy(() -> service.listarCatalogo("ana@mail.com", 99L))
				.isInstanceOf(UniversidadNoEncontradaException.class).hasMessageContaining("99");
		verify(usuarioRepository, never()).listarPorUniversidad(any(), any(), any());
	}

	@Test
	void rankingGeneralConservaElOrdenDelRepositorioYAgregaLosConteos() {
		when(usuarioRepository.rankingPorMeGusta(Rol.ESTUDIANTE, null, 20)).thenReturn(List.of(marta, luis));
		when(reaccionRepository.contarPorDestinatarios(List.of(3L, 2L)))
				.thenReturn(List.of(new ConteoReaccionesDTO(3L, 9L), new ConteoReaccionesDTO(2L, 4L)));
		when(reaccionRepository.destinatariosReaccionados(1L, List.of(3L, 2L))).thenReturn(List.of(2L));

		List<UsuarioPublicoDTO> ranking = service.rankingMeGusta("ana@mail.com", null, 20);

		assertThat(ranking).extracting(UsuarioPublicoDTO::getId).containsExactly(3L, 2L);
		assertThat(ranking).extracting(UsuarioPublicoDTO::getTotalMeGusta).containsExactly(9L, 4L);
		assertThat(ranking.get(1).isMeGustaDelUsuarioActual()).isTrue();
		verifyNoInteractions(universidadRepository);
	}

	@Test
	void rankingPorUniversidadFiltraEnElRepositorio() {
		when(universidadRepository.existePorId(10L)).thenReturn(true);
		when(usuarioRepository.rankingPorMeGusta(Rol.ESTUDIANTE, 10L, 20)).thenReturn(List.of(luis));

		List<UsuarioPublicoDTO> ranking = service.rankingMeGusta("ana@mail.com", 10L, 20);

		assertThat(ranking).extracting(UsuarioPublicoDTO::getId).containsExactly(2L);
	}

	@Test
	void rankingConLimiteFueraDeRangoDa400() {
		assertThatThrownBy(() -> service.rankingMeGusta("ana@mail.com", null, 0))
				.isInstanceOf(LimiteRankingInvalidoException.class);
		assertThatThrownBy(() -> service.rankingMeGusta("ana@mail.com", null, 101))
				.isInstanceOf(LimiteRankingInvalidoException.class);
	}

	@Test
	void rankingVacioNoConsultaReacciones() {
		when(usuarioRepository.rankingPorMeGusta(Rol.ESTUDIANTE, null, 20)).thenReturn(List.of());

		assertThat(service.rankingMeGusta("ana@mail.com", null, 20)).isEmpty();
		verifyNoInteractions(reaccionRepository);
	}

	@Test
	void catalogoVacioNoConsultaReacciones() {
		when(usuarioRepository.listarPorRolExcepto(Rol.ESTUDIANTE, 1L)).thenReturn(List.of());

		assertThat(service.listarCatalogo("ana@mail.com", null)).isEmpty();
		verifyNoInteractions(reaccionRepository);
	}

	@Test
	void perfilPublicoDeUnaCuentaNoVisibleDa404() {
		when(usuarioFinder.estudianteVisible(50L)).thenThrow(new UsuarioNoEncontradoPorIdException(50L));

		assertThatThrownBy(() -> service.obtenerPerfilPublico("ana@mail.com", 50L))
				.isInstanceOf(UsuarioNoEncontradoPorIdException.class);
	}

	// ------------------------------------------------------------------
	// Perfil propio
	// ------------------------------------------------------------------

	@Test
	void perfilPropioIncluyeDatosPrivados() {
		UsuarioPerfilDTO dto = service.obtenerPerfilPropio("ana@mail.com");

		assertThat(dto.getCorreo()).isEqualTo("ana@mail.com");
		assertThat(dto.getFechaNacimiento()).isNotNull();
	}

	@Test
	void actualizarPerfilPropioAplicaCamposYColecciones() {
		List<EstudioRequestDTO> estudios = List.of(new EstudioRequestDTO(1L, 10L, 5));
		ActualizarPerfilRequestDTO request = new ActualizarPerfilRequestDTO("Anita", "Gomez", Genero.FEMENINO,
				LocalDate.now().minusYears(25), "foto.png", Set.of("cine"), estudios, List.of());

		UsuarioPerfilDTO dto = service.actualizarPerfilPropio("ana@mail.com", request);

		assertThat(dto.getNombre()).isEqualTo("Anita");
		assertThat(dto.getApellido()).isEqualTo("Gomez");
		assertThat(dto.getGenero()).isEqualTo(Genero.FEMENINO);
		assertThat(dto.getFotoPerfil()).isEqualTo("foto.png");
		verify(perfilAssembler).validarEdadMinima(request.getFechaNacimiento());
		verify(perfilAssembler).aplicarIntereses(ana, request.getIntereses());
		verify(perfilAssembler).sincronizarEstudios(ana, estudios);
		verify(perfilAssembler).sincronizarRedes(ana, request.getRedes());
		// el correo NO se puede cambiar desde el perfil propio
		assertThat(ana.getCorreo()).isEqualTo("ana@mail.com");
	}

	@Test
	void eliminarCuentaPropiaBorraAlUsuario() {
		service.eliminarCuentaPropia("ana@mail.com");

		verify(usuarioRepository).eliminar(ana);
	}

	// ------------------------------------------------------------------
	// Administracion
	// ------------------------------------------------------------------

	private AdminUsuarioRequestDTO adminRequest(String correo, String contrasena, Rol rol) {
		return new AdminUsuarioRequestDTO(correo, contrasena, rol, null, "Nuevo", "Usuario", null, null, null, null,
				null);
	}

	@Test
	void adminCreaUsuarioConContrasenaCifradaYRol() {
		UsuarioPerfilDTO dto = service.crearPorAdmin(adminRequest("nuevo@mail.com", "secreto123", Rol.ADMIN));

		ArgumentCaptor<Usuario> captor = ArgumentCaptor.forClass(Usuario.class);
		verify(usuarioRepository).guardar(captor.capture());
		assertThat(captor.getValue().getContrasenaHash()).isEqualTo("ENC");
		assertThat(dto.getRol()).isEqualTo(Rol.ADMIN);
		assertThat(dto.getCorreo()).isEqualTo("nuevo@mail.com");
	}

	@Test
	void adminNoPuedeCrearConCorreoRepetido() {
		when(usuarioRepository.existePorCorreo("repetido@mail.com")).thenReturn(true);
		assertThatThrownBy(() -> service.crearPorAdmin(adminRequest("repetido@mail.com", "secreto123", null)))
				.isInstanceOf(CorreoUsuarioYaExisteException.class);

		verify(usuarioRepository, never()).guardar(any());
	}

	@Test
	void adminDebeIndicarContrasenaAlCrear() {
		assertThatThrownBy(() -> service.crearPorAdmin(adminRequest("nuevo@mail.com", " ", null)))
				.isInstanceOf(ContrasenaObligatoriaAlCrearException.class).hasMessageContaining("contrasena");
		assertThatThrownBy(() -> service.crearPorAdmin(adminRequest("nuevo@mail.com", null, null)))
				.isInstanceOf(ContrasenaObligatoriaAlCrearException.class);
	}

	@Test
	void adminActualizaSinContrasenaConservaElHash() {
		when(usuarioFinder.porId(2L)).thenReturn(luis);

		UsuarioPerfilDTO dto = service.actualizarPorAdmin(2L, adminRequest("luis@mail.com", null, null));

		assertThat(luis.getContrasenaHash()).isEqualTo("hash-2");
		assertThat(dto.getNombre()).isEqualTo("Nuevo");
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
		when(usuarioRepository.existePorCorreoExceptoId("ana@mail.com", 2L)).thenReturn(true);

		assertThatThrownBy(() -> service.actualizarPorAdmin(2L, adminRequest("ana@mail.com", null, null)))
				.isInstanceOf(CorreoUsuarioYaExisteException.class);
		verify(usuarioRepository, never()).guardar(any());
	}

	@Test
	void adminEliminaUsuarioInexistenteDa404() {
		when(usuarioFinder.porId(77L)).thenThrow(new UsuarioNoEncontradoPorIdException(77L));

		assertThatThrownBy(() -> service.eliminarPorAdmin(77L)).isInstanceOf(UsuarioNoEncontradoPorIdException.class);
		verify(usuarioRepository, never()).eliminar(any());
	}

	@Test
	void adminEliminaUsuario() {
		when(usuarioFinder.porId(2L)).thenReturn(luis);

		service.eliminarPorAdmin(2L);

		verify(usuarioRepository).eliminar(luis);
	}

	@Test
	void adminListaPerfilesCompletos() {
		when(usuarioRepository.listarTodos()).thenReturn(List.of(ana, luis));

		assertThat(service.listarParaAdmin()).extracting(UsuarioPerfilDTO::getCorreo).containsExactly("ana@mail.com",
				"luis@mail.com");
	}

	@Test
	void adminCreaEstudianteValidandoLasMismasReglasQueElRegistro() {
		service.crearPorAdmin(adminRequest("nuevo@mail.com", "secreto123", null));

		verify(perfilAssembler).validarCorreo("nuevo@mail.com");
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

		assertThatThrownBy(() -> service.eliminarPorAdmin(9L)).isInstanceOf(UltimoAdministradorEliminadoException.class);
		verify(usuarioRepository, never()).eliminar(any());
	}

	@Test
	void sePuedeEliminarUnAdministradorSiQuedaOtro() {
		Usuario admin = TestData.usuario(9L, "root", "Root", "Admin");
		admin.setRol(Rol.ADMIN);
		when(usuarioFinder.porId(9L)).thenReturn(admin);
		when(usuarioRepository.contarPorRol(Rol.ADMIN)).thenReturn(2L);

		service.eliminarPorAdmin(9L);

		verify(usuarioRepository).eliminar(admin);
	}

	@Test
	void noSePuedeQuitarElRolAlUltimoAdministrador() {
		Usuario admin = TestData.usuario(9L, "root", "Root", "Admin");
		admin.setRol(Rol.ADMIN);
		when(usuarioFinder.porId(9L)).thenReturn(admin);
		when(usuarioRepository.contarPorRol(Rol.ADMIN)).thenReturn(1L);

		assertThatThrownBy(() -> service.actualizarPorAdmin(9L, adminRequest("root", null, Rol.ESTUDIANTE)))
				.isInstanceOf(UltimoAdministradorSinRolException.class);
		verify(usuarioRepository, never()).guardar(any());
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