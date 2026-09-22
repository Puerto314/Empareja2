package co.edu.unbosque.empareja2.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.unbosque.empareja2.dto.request.UpdateProfileRequestDTO;
import co.edu.unbosque.empareja2.dto.response.StudentProfileDTO;
import co.edu.unbosque.empareja2.dto.response.StudentPublicDTO;
import co.edu.unbosque.empareja2.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

/**
 * Expone el catalogo de estudiantes. Todos los endpoints requieren
 * autenticacion (ver SecurityConfig): solo un usuario logueado puede ver la
 * informacion no sensible de los demas.
 */
@RestController
@RequestMapping("/api/students")
@Tag(name = "Estudiantes", description = "Catalogo de estudiantes y perfil propio")
public class StudentController {

	private final StudentService studentService;

	public StudentController(StudentService studentService) {
		this.studentService = studentService;
	}

	@GetMapping
	@Operation(summary = "Lista la informacion publica de todos los estudiantes registrados")
	public ResponseEntity<List<StudentPublicDTO>> getAll() {
		return ResponseEntity.ok(studentService.getAllPublicProfiles());
	}

	@GetMapping("/{id}")
	@Operation(summary = "Obtiene la informacion publica de un estudiante por id")
	public ResponseEntity<StudentPublicDTO> getById(@PathVariable Long id) {
		return ResponseEntity.ok(studentService.getPublicProfileById(id));
	}

	@GetMapping("/me")
	@Operation(summary = "Obtiene el perfil completo del usuario autenticado")
	public ResponseEntity<StudentProfileDTO> getOwnProfile(Authentication authentication) {
		String email = authentication.getName();
		return ResponseEntity.ok(studentService.getOwnProfile(email));
	}

	@PutMapping("/me")
	@Operation(summary = "Actualiza el perfil del usuario autenticado")
	public ResponseEntity<StudentProfileDTO> updateOwnProfile(Authentication authentication,
			@Valid @RequestBody UpdateProfileRequestDTO request) {
		String email = authentication.getName();
		return ResponseEntity.ok(studentService.updateOwnProfile(email, request));
	}

	@DeleteMapping("/me")
	@Operation(summary = "Elimina la cuenta del usuario autenticado")
	public ResponseEntity<Void> deleteOwnAccount(Authentication authentication) {
		String email = authentication.getName();
		studentService.deleteOwnAccount(email);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
}
