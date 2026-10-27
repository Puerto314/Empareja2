package co.edu.unbosque.empareja2.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import co.edu.unbosque.empareja2.TestData;
import co.edu.unbosque.empareja2.dto.request.EstudioRequestDTO;
import co.edu.unbosque.empareja2.dto.request.LoginRequestDTO;
import co.edu.unbosque.empareja2.dto.request.RegistroRequestDTO;
import co.edu.unbosque.empareja2.dto.response.AuthResponseDTO;
import co.edu.unbosque.empareja2.entity.Genero;
import co.edu.unbosque.empareja2.entity.Rol;
import co.edu.unbosque.empareja2.entity.TipoDocumento;
import co.edu.unbosque.empareja2.entity.Usuario;
import co.edu.unbosque.empareja2.exception.DuplicateResourceException;
import co.edu.unbosque.empareja2.mapper.UsuarioMapper;
import co.edu.unbosque.empareja2.repository.UsuarioRepository;
import co.edu.unbosque.empareja2.security.CustomUserDetailsService;
import co.edu.unbosque.empareja2.security.JwtService;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class AuthServiceImplTest {

    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private PerfilAssembler perfilAssembler;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtService jwtService;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private CustomUserDetailsService userDetailsService;

    private AuthServiceImpl service;

    private RegistroRequestDTO registro;
    private final UserDetails userDetails = User.builder().username("ana@mail.com").password("ENC")
            .authorities("ROLE_ESTUDIANTE").build();

    @BeforeEach
    void preparar() {
        service = new AuthServiceImpl(usuarioRepository, new UsuarioMapper(), perfilAssembler, passwordEncoder,
                jwtService, authenticationManager, userDetailsService);

        registro = new RegistroRequestDTO("ana@mail.com", "secreto123", TipoDocumento.CC, "123", Genero.FEMENINO,
                "Ana", "Garcia", LocalDate.now().minusYears(21), null, Set.of("cine"),
                List.of(new EstudioRequestDTO(1L, 10L, 3)), List.of());

        when(usuarioRepository.existsByCorreo(any())).thenReturn(false);
        when(usuarioRepository.existsByTipoDocumentoAndDocumento(any(), any())).thenReturn(false);
        when(passwordEncoder.encode("secreto123")).thenReturn("ENC");
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(inv -> {
            Usuario u = inv.getArgument(0);
            u.setId(1L);
            return u;
        });
        when(userDetailsService.loadUserByUsername("ana@mail.com")).thenReturn(userDetails);
        when(jwtService.generateToken(userDetails)).thenReturn("token-jwt");
        when(jwtService.getExpirationMs()).thenReturn(86_400_000L);
    }

    @Test
    void registroExitosoGuardaConHashYDevuelveToken() {
        AuthResponseDTO respuesta = service.registrar(registro);

        ArgumentCaptor<Usuario> captor = ArgumentCaptor.forClass(Usuario.class);
        verify(usuarioRepository).save(captor.capture());
        Usuario guardado = captor.getValue();
        assertThat(guardado.getContrasenaHash()).isEqualTo("ENC");
        assertThat(guardado.getRol()).isEqualTo(Rol.ESTUDIANTE);
        assertThat(guardado.getCorreo()).isEqualTo("ana@mail.com");

        assertThat(respuesta.token()).isEqualTo("token-jwt");
        assertThat(respuesta.tokenType()).isEqualTo("Bearer");
        assertThat(respuesta.expiresInMs()).isEqualTo(86_400_000L);
        assertThat(respuesta.usuario().correo()).isEqualTo("ana@mail.com");
        assertThat(respuesta.usuario().nombreCompleto()).isEqualTo("Ana Garcia");
    }

    @Test
    void registroDelegaLasColeccionesAlEnsamblador() {
        service.registrar(registro);

        verify(perfilAssembler).validarEdadMinima(registro.fechaNacimiento());
        verify(perfilAssembler).aplicarIntereses(any(Usuario.class), any());
        verify(perfilAssembler).sincronizarEstudios(any(Usuario.class), any());
        verify(perfilAssembler).sincronizarRedes(any(Usuario.class), any());
    }

    @Test
    void registroConCorreoRepetidoLanzaConflicto() {
        when(usuarioRepository.existsByCorreo("ana@mail.com")).thenReturn(true);

        assertThatThrownBy(() -> service.registrar(registro)).isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("correo");
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void registroConDocumentoRepetidoLanzaConflicto() {
        when(usuarioRepository.existsByTipoDocumentoAndDocumento(TipoDocumento.CC, "123")).thenReturn(true);

        assertThatThrownBy(() -> service.registrar(registro)).isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("documento");
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void registroDeMenorDeEdadSeRechazaYNoGuarda() {
        doThrow(new IllegalArgumentException("Debes tener al menos 18 anos")).when(perfilAssembler)
                .validarEdadMinima(any());

        assertThatThrownBy(() -> service.registrar(registro)).isInstanceOf(IllegalArgumentException.class);
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void loginExitosoAutenticaYDevuelveToken() {
        Usuario existente = TestData.usuario(1L, "ana@mail.com", "Ana", "Garcia");
        when(usuarioRepository.findByCorreo("ana@mail.com")).thenReturn(Optional.of(existente));

        AuthResponseDTO respuesta = service.iniciarSesion(new LoginRequestDTO("ana@mail.com", "secreto123"));

        verify(authenticationManager).authenticate(new UsernamePasswordAuthenticationToken("ana@mail.com", "secreto123"));
        assertThat(respuesta.token()).isEqualTo("token-jwt");
        assertThat(respuesta.usuario().id()).isEqualTo(1L);
    }

    @Test
    void loginConCredencialesInvalidasPropagaLaExcepcion() {
        when(authenticationManager.authenticate(any())).thenThrow(new BadCredentialsException("malas"));

        assertThatThrownBy(() -> service.iniciarSesion(new LoginRequestDTO("ana@mail.com", "x")))
                .isInstanceOf(BadCredentialsException.class);
        verify(jwtService, never()).generateToken(any());
    }
}
