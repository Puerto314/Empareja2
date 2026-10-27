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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.edu.unbosque.empareja2.dto.request.ActualizarPerfilRequestDTO;
import co.edu.unbosque.empareja2.dto.response.UsuarioPerfilDTO;
import co.edu.unbosque.empareja2.dto.response.UsuarioPublicoDTO;
import co.edu.unbosque.empareja2.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

/**
 * Catalogo de usuarios y perfil propio. Todos los endpoints requieren
 * autenticacion: solo un usuario logueado ve la informacion no sensible de los
 * demas.
 */
@RestController
@RequestMapping("/api/usuarios")
@Tag(name = "Usuarios", description = "Catalogo de usuarios y perfil propio")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    @Operation(summary = "Lista los demas usuarios registrados (solo informacion publica); "
            + "con ?universidadId=N filtra por universidad")
    public ResponseEntity<List<UsuarioPublicoDTO>> listarCatalogo(Authentication authentication,
            @RequestParam(required = false) Long universidadId) {
        return ResponseEntity.ok(usuarioService.listarCatalogo(authentication.getName(), universidadId));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtiene la informacion publica de un usuario por id")
    public ResponseEntity<UsuarioPublicoDTO> obtenerPerfilPublico(Authentication authentication,
            @PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.obtenerPerfilPublico(authentication.getName(), id));
    }

    @GetMapping("/me")
    @Operation(summary = "Obtiene el perfil completo del usuario autenticado")
    public ResponseEntity<UsuarioPerfilDTO> obtenerPerfilPropio(Authentication authentication) {
        return ResponseEntity.ok(usuarioService.obtenerPerfilPropio(authentication.getName()));
    }

    @PutMapping("/me")
    @Operation(summary = "Actualiza el perfil del usuario autenticado")
    public ResponseEntity<UsuarioPerfilDTO> actualizarPerfilPropio(Authentication authentication,
            @Valid @RequestBody ActualizarPerfilRequestDTO request) {
        return ResponseEntity.ok(usuarioService.actualizarPerfilPropio(authentication.getName(), request));
    }

    @DeleteMapping("/me")
    @Operation(summary = "Elimina la cuenta del usuario autenticado")
    public ResponseEntity<Void> eliminarCuentaPropia(Authentication authentication) {
        usuarioService.eliminarCuentaPropia(authentication.getName());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
