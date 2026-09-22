package co.edu.unbosque.empareja2.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.unbosque.empareja2.dto.request.AdminUserRequestDTO;
import co.edu.unbosque.empareja2.dto.response.StudentProfileDTO;
import co.edu.unbosque.empareja2.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

/**
 * CRUD completo de usuarios (estudiantes y administradores). Todos los
 * endpoints de este controlador estan restringidos al rol ADMIN en
 * SecurityConfig ("/api/admin/**"). Un usuario normal jamas puede administrar
 * cuentas ajenas: solo la propia, a traves de StudentController.
 */
@RestController
@RequestMapping("/api/admin/users")
@Tag(name = "Administracion", description = "CRUD de usuarios y administradores (solo ADMIN)")
public class AdminController {

	private final StudentService studentService;

	public AdminController(StudentService studentService) {
		this.studentService = studentService;
	}

	@GetMapping
	@Operation(summary = "Lista todos los usuarios del sistema (estudiantes y administradores)")
	public ResponseEntity<List<StudentProfileDTO>> getAll() {
		return ResponseEntity.ok(studentService.getAllUsersForAdmin());
	}

	@GetMapping("/{id}")
	@Operation(summary = "Obtiene el perfil completo de cualquier usuario por id")
	public ResponseEntity<StudentProfileDTO> getById(@PathVariable Long id) {
		return ResponseEntity.ok(studentService.getUserByIdForAdmin(id));
	}

	@PostMapping
	@Operation(summary = "Crea un usuario nuevo (estudiante o administrador)")
	public ResponseEntity<StudentProfileDTO> create(@Valid @RequestBody AdminUserRequestDTO request) {
		StudentProfileDTO created = studentService.createUserByAdmin(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(created);
	}

	@PutMapping("/{id}")
	@Operation(summary = "Edita cualquier usuario existente")
	public ResponseEntity<StudentProfileDTO> update(@PathVariable Long id,
			@Valid @RequestBody AdminUserRequestDTO request) {
		return ResponseEntity.ok(studentService.updateUserByAdmin(id, request));
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Elimina cualquier usuario existente")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		studentService.deleteUserByAdmin(id);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
}
