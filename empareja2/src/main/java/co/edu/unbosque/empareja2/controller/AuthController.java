package co.edu.unbosque.empareja2.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.unbosque.empareja2.dto.request.LoginRequestDTO;
import co.edu.unbosque.empareja2.dto.request.RegisterRequestDTO;
import co.edu.unbosque.empareja2.dto.response.AuthResponseDTO;
import co.edu.unbosque.empareja2.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

/**
 * Expone los endpoints publicos de autenticacion: registro e inicio de sesion.
 * Ambos devuelven un token JWT que debe usarse en el header "Authorization:
 * Bearer {token}" para consumir el resto de la API.
 */
@RestController
@RequestMapping("/api/auth")
@Tag(name = "Autenticacion", description = "Registro e inicio de sesion de estudiantes")
public class AuthController {

	private final AuthService authService;

	public AuthController(AuthService authService) {
		this.authService = authService;
	}

	@PostMapping("/register")
	@Operation(summary = "Registra un nuevo estudiante en el catalogo")
	public ResponseEntity<AuthResponseDTO> register(@Valid @RequestBody RegisterRequestDTO request) {
		AuthResponseDTO response = authService.register(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@PostMapping("/login")
	@Operation(summary = "Inicia sesion con correo y contrasena")
	public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {
		AuthResponseDTO response = authService.login(request);
		return ResponseEntity.ok(response);
	}
}
