package co.edu.unbosque.empareja2.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.unbosque.empareja2.dto.AuthResponseDTO;
import co.edu.unbosque.empareja2.dto.LoginRequestDTO;
import co.edu.unbosque.empareja2.dto.RegistroRequestDTO;
import co.edu.unbosque.empareja2.service.AuthService;
import jakarta.validation.Valid;

/**
 * Endpoints publicos de autenticacion: registro e inicio de sesion. Ambos
 * devuelven un token JWT para el header "Authorization: Bearer {token}".
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    /** Servicio de registro e inicio de sesion. */
    private final AuthService authService;

    /**
     * Crea el controlador.
     *
     * @param authService el servicio de autenticacion
     */
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Registra un nuevo usuario en el catalogo.
     *
     * @param request los datos de registro
     * @return el token y el perfil del usuario creado (201)
     */
    @PostMapping("/registro")
    public ResponseEntity<AuthResponseDTO> registrar(@Valid @RequestBody RegistroRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.registrar(request));
    }

    /**
     * Inicia sesion con correo y contrasena.
     *
     * @param request las credenciales
     * @return el token y el perfil del usuario
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {
        return ResponseEntity.ok(authService.iniciarSesion(request));
    }
}
