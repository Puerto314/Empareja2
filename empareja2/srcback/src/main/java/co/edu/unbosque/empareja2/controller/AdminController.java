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

import co.edu.unbosque.empareja2.dto.request.AdminUsuarioRequestDTO;
import co.edu.unbosque.empareja2.dto.response.UsuarioPerfilDTO;
import co.edu.unbosque.empareja2.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

/**
 * Administracion de usuarios (CRUD completo). Todo "/api/admin/**" esta
 * restringido al rol ADMIN en SecurityConfig. Los catalogos estan en
 * AdminCatalogoController y la moderacion en AdminModeracionController.
 */
@RestController
@RequestMapping("/api/admin")
@Tag(name = "Administracion - Usuarios", description = "CRUD de usuarios (solo ADMIN)")
public class AdminController {

    private final UsuarioService usuarioService;

    public AdminController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // ---- Usuarios ----

    @GetMapping("/usuarios")
    @Operation(summary = "Lista todos los usuarios con su perfil completo")
    public ResponseEntity<List<UsuarioPerfilDTO>> listarUsuarios() {
        return ResponseEntity.ok(usuarioService.listarParaAdmin());
    }

    @GetMapping("/usuarios/{id}")
    @Operation(summary = "Obtiene el perfil completo de cualquier usuario")
    public ResponseEntity<UsuarioPerfilDTO> obtenerUsuario(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.obtenerParaAdmin(id));
    }

    @PostMapping("/usuarios")
    @Operation(summary = "Crea un usuario (estudiante o administrador)")
    public ResponseEntity<UsuarioPerfilDTO> crearUsuario(@Valid @RequestBody AdminUsuarioRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.crearPorAdmin(request));
    }

    @PutMapping("/usuarios/{id}")
    @Operation(summary = "Edita cualquier usuario")
    public ResponseEntity<UsuarioPerfilDTO> actualizarUsuario(@PathVariable Long id,
            @Valid @RequestBody AdminUsuarioRequestDTO request) {
        return ResponseEntity.ok(usuarioService.actualizarPorAdmin(id, request));
    }

    @DeleteMapping("/usuarios/{id}")
    @Operation(summary = "Elimina cualquier usuario")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id) {
        usuarioService.eliminarPorAdmin(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
