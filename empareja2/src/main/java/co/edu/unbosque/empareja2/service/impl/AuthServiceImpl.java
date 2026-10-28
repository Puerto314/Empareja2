package co.edu.unbosque.empareja2.service.impl;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.unbosque.empareja2.dto.request.LoginRequestDTO;
import co.edu.unbosque.empareja2.dto.request.RegistroRequestDTO;
import co.edu.unbosque.empareja2.dto.response.AuthResponseDTO;
import co.edu.unbosque.empareja2.entity.Usuario;
import co.edu.unbosque.empareja2.exception.DuplicateResourceException;
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

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;
    private final PerfilAssembler perfilAssembler;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;

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

    @Override
    @Transactional
    public AuthResponseDTO registrar(RegistroRequestDTO request) {
        if (usuarioRepository.existsByCorreo(request.correo())) {
            throw new DuplicateResourceException("Ya existe un usuario registrado con ese correo");
        }
        if (usuarioRepository.existsByTipoDocumentoAndDocumento(request.tipoDocumento(), request.documento())) {
            throw new DuplicateResourceException("Ya existe un usuario registrado con ese documento");
        }
        perfilAssembler.validarDocumento(request.tipoDocumento(), request.documento());
        perfilAssembler.validarEdadMinima(request.fechaNacimiento());

        Usuario usuario = usuarioMapper.toEntity(request, passwordEncoder.encode(request.contrasena()));
        perfilAssembler.aplicarIntereses(usuario, request.intereses());
        perfilAssembler.sincronizarEstudios(usuario, request.estudios());
        perfilAssembler.sincronizarRedes(usuario, request.redes());

        Usuario guardado = usuarioRepository.save(usuario);
        return construirRespuesta(guardado);
    }

    @Override
    @Transactional(readOnly = true)
    public AuthResponseDTO iniciarSesion(LoginRequestDTO request) {
        // Si las credenciales son invalidas lanza BadCredentialsException, que
        // captura GlobalExceptionHandler (401).
        authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(request.correo(), request.contrasena()));

        Usuario usuario = usuarioRepository.findByCorreo(request.correo()).orElseThrow(
                () -> new IllegalStateException("Usuario autenticado pero no encontrado en base de datos"));
        return construirRespuesta(usuario);
    }

    private AuthResponseDTO construirRespuesta(Usuario usuario) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(usuario.getCorreo());
        String token = jwtService.generateToken(userDetails);
        return AuthResponseDTO.bearer(token, jwtService.getExpirationMs(), usuarioMapper.toPerfilDTO(usuario));
    }
}
