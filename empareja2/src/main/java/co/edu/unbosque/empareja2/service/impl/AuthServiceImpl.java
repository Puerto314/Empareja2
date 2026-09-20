package co.edu.unbosque.empareja2.service.impl;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.unbosque.empareja2.dto.request.LoginRequestDTO;
import co.edu.unbosque.empareja2.dto.request.RegisterRequestDTO;
import co.edu.unbosque.empareja2.dto.response.AuthResponseDTO;
import co.edu.unbosque.empareja2.entity.Student;
import co.edu.unbosque.empareja2.exception.DuplicateResourceException;
import co.edu.unbosque.empareja2.mapper.StudentMapper;
import co.edu.unbosque.empareja2.repository.StudentRepository;
import co.edu.unbosque.empareja2.security.CustomUserDetailsService;
import co.edu.unbosque.empareja2.security.JwtService;
import co.edu.unbosque.empareja2.service.AuthService;

/**
 * Implementa el registro y el inicio de sesion, delegando la validacion
 * de credenciales al AuthenticationManager de Spring Security y la
 * generacion del token al JwtService.
 */
@Service
public class AuthServiceImpl implements AuthService {

    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;

    public AuthServiceImpl(StudentRepository studentRepository, StudentMapper studentMapper,
            PasswordEncoder passwordEncoder, JwtService jwtService,
            AuthenticationManager authenticationManager, CustomUserDetailsService userDetailsService) {
        this.studentRepository = studentRepository;
        this.studentMapper = studentMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
    }

    @Override
    @Transactional
    public AuthResponseDTO register(RegisterRequestDTO request) {
        if (studentRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Ya existe un usuario registrado con ese correo");
        }

        String encodedPassword = passwordEncoder.encode(request.getPassword());
        Student student = studentMapper.toEntity(request, encodedPassword);
        Student saved = studentRepository.save(student);

        UserDetails userDetails = userDetailsService.loadUserByUsername(saved.getEmail());
        String token = jwtService.generateToken(userDetails);

        return buildAuthResponse(token, saved);
    }

    @Override
    public AuthResponseDTO login(LoginRequestDTO request) {
        // Si las credenciales son invalidas, esto lanza BadCredentialsException,
        // capturada de forma centralizada por el GlobalExceptionHandler.
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        Student student = studentRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalStateException("Usuario autenticado pero no encontrado en base de datos"));

        UserDetails userDetails = userDetailsService.loadUserByUsername(student.getEmail());
        String token = jwtService.generateToken(userDetails);

        return buildAuthResponse(token, student);
    }

    private AuthResponseDTO buildAuthResponse(String token, Student student) {
        return new AuthResponseDTO(token, jwtService.getExpirationMs(), studentMapper.toProfileDTO(student));
    }
}
