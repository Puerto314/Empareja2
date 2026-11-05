package co.edu.unbosque.empareja2.service.impl;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.unbosque.empareja2.dto.AuthResponseDTO;
import co.edu.unbosque.empareja2.dto.LoginRequestDTO;
import co.edu.unbosque.empareja2.dto.RegistroRequestDTO;
import co.edu.unbosque.empareja2.entity.Usuario;
import co.edu.unbosque.empareja2.exception.LanzadorException;
import co.edu.unbosque.empareja2.mapper.UsuarioMapper;
import co.edu.unbosque.empareja2.repository.UsuarioRepository;
import co.edu.unbosque.empareja2.security.CustomUserDetailsService;
import co.edu.unbosque.empareja2.security.JwtService;
import co.edu.unbosque.empareja2.service.AuthService;

/**
 * Registro e inicio de sesion. La validacion de credenciales la hace el
 * AuthenticationManager de Spring Security y el token lo genera JwtService.
 */
@Service
public class AuthServiceImpl implements AuthService {

    /** Tipo de token que se devuelve al cliente. */
    private static final String TIPO_TOKEN = "Bearer";

    /** Acceso a los datos de los usuarios. */
    private final UsuarioRepository usuarioRepository;

    /** Convierte entre usuarios y DTOs. */
    private final UsuarioMapper usuarioMapper;

    /** Arma los intereses, estudios y redes del perfil. */
    private final PerfilAssembler perfilAssembler;

    /** Cifra las contrasenas. */
    private final PasswordEncoder passwordEncoder;

    /** Genera los tokens JWT. */
    private final JwtService jwtService;

    /** Valida las credenciales con Spring Security. */
    private final AuthenticationManager authenticationManager;

    /** Carga los datos de acceso de un usuario. */
    private final CustomUserDetailsService userDetailsService;

    /**
     * Crea el servicio.
     *
     * @param usuarioRepository     el repositorio de usuarios
     * @param usuarioMapper         el convertidor de usuarios
     * @param perfilAssembler       el armador de perfiles
     * @param passwordEncoder       el cifrador de contrasenas
     * @param jwtService            el generador de tokens
     * @param authenticationManager el validador de credenciales
     * @param userDetailsService    el cargador de datos de acceso
     */
    public AuthServiceImpl(UsuarioRepository usuarioRepository, UsuarioMapper usuarioMapper,
            PerfilAssembler perfilAssembler, PasswordEncoder passwordEncoder, JwtService jwtService,
            AuthenticationManager authenticationManager, CustomUserDetailsService userDetailsService) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioMapper = usuarioMapper;
        this.perfilAssembler = perfilAssembler;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
    }

    /**
     * Registra un estudiante nuevo y le abre sesion.
     *
     * @param request los datos de registro
     * @return el token y el perfil del usuario creado
     */
    @Override
    @Transactional
    public AuthResponseDTO registrar(RegistroRequestDTO request) {
        if (usuarioRepository.existePorCorreo(request.getCorreo())) {
            LanzadorException.correoYaRegistrado();
        }
        perfilAssembler.exigirDatosDeEstudiante(request.getFechaNacimiento(), request.getEstudios());
        perfilAssembler.validarEdadMinima(request.getFechaNacimiento());

        Usuario usuario = usuarioMapper.toEntity(request, passwordEncoder.encode(request.getContrasena()));
        perfilAssembler.aplicarIntereses(usuario, request.getIntereses());
        perfilAssembler.sincronizarEstudios(usuario, request.getEstudios());
        perfilAssembler.sincronizarRedes(usuario, request.getRedes());

        Usuario guardado = usuarioRepository.guardar(usuario);
        return construirRespuesta(guardado);
    }

    /**
     * Inicia sesion con correo y contrasena. Si las credenciales son invalidas el
     * AuthenticationManager lanza BadCredentialsException, que captura
     * GlobalExceptionHandler (401).
     *
     * @param request las credenciales
     * @return el token y el perfil del usuario
     */
    @Override
    @Transactional(readOnly = true)
    public AuthResponseDTO iniciarSesion(LoginRequestDTO request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getCorreo(), request.getContrasena()));

        Usuario usuario = usuarioRepository.buscarPorCorreo(request.getCorreo()).orElse(null);
        if (usuario == null) {
            LanzadorException.usuarioAutenticadoNoEncontrado();
        }
        return construirRespuesta(usuario);
    }

    /**
     * Arma la respuesta con el token JWT y el perfil del usuario.
     *
     * @param usuario el usuario autenticado
     * @return la respuesta de autenticacion
     */
    private AuthResponseDTO construirRespuesta(Usuario usuario) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(usuario.getCorreo());
        String token = jwtService.generateToken(userDetails);
        return new AuthResponseDTO(token, TIPO_TOKEN, jwtService.getExpirationMs(),
                usuarioMapper.toPerfilDTO(usuario));
    }
}
