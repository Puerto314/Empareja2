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

import co.edu.unbosque.empareja2.dto.AdminUsuarioRequestDTO;
import co.edu.unbosque.empareja2.dto.UsuarioPerfilDTO;
import co.edu.unbosque.empareja2.service.UsuarioService;
import jakarta.validation.Valid;

/**
 * Administracion de usuarios (CRUD completo). Todo "/api/admin/**" esta
 * restringido al rol ADMIN en SecurityConfig. Los catalogos estan en
 * AdminCatalogoController y la moderacion en AdminModeracionController.
 */
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    /** Servicio de usuarios. */
    private final UsuarioService usuarioService;

    /**
     * Crea el controlador.
     *
     * @param usuarioService el servicio de usuarios
     */
    public AdminController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    /**
     * Lista todos los usuarios con su perfil completo.
     *
     * @return los usuarios
     */
    @GetMapping("/usuarios")
    public ResponseEntity<List<UsuarioPerfilDTO>> listarUsuarios() {
        return ResponseEntity.ok(usuarioService.listarParaAdmin());
    }

    /**
     * Obtiene el perfil completo de cualquier usuario.
     *
     * @param id el identificador del usuario
     * @return su perfil completo
     */
    @GetMapping("/usuarios/{id}")
    public ResponseEntity<UsuarioPerfilDTO> obtenerUsuario(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.obtenerParaAdmin(id));
    }

    /**
     * Crea un usuario (estudiante o administrador).
     *
     * @param request los datos del usuario
     * @return el usuario creado (201)
     */
    @PostMapping("/usuarios")
    public ResponseEntity<UsuarioPerfilDTO> crearUsuario(@Valid @RequestBody AdminUsuarioRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.crearPorAdmin(request));
    }

    /**
     * Edita cualquier usuario.
     *
     * @param id      el identificador del usuario
     * @param request los datos nuevos
     * @return el usuario editado
     */
    @PutMapping("/usuarios/{id}")
    public ResponseEntity<UsuarioPerfilDTO> actualizarUsuario(@PathVariable Long id,
            @Valid @RequestBody AdminUsuarioRequestDTO request) {
        return ResponseEntity.ok(usuarioService.actualizarPorAdmin(id, request));
    }

    /**
     * Elimina cualquier usuario.
     *
     * @param id el identificador del usuario
     * @return respuesta vacia (204)
     */
    @DeleteMapping("/usuarios/{id}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id) {
        usuarioService.eliminarPorAdmin(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
