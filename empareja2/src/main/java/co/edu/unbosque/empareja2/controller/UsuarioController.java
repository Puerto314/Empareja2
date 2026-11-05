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

import co.edu.unbosque.empareja2.dto.ActualizarPerfilRequestDTO;
import co.edu.unbosque.empareja2.dto.UsuarioPerfilDTO;
import co.edu.unbosque.empareja2.dto.UsuarioPublicoDTO;
import co.edu.unbosque.empareja2.service.UsuarioService;
import jakarta.validation.Valid;

/**
 * Catalogo de usuarios y perfil propio. Todos los endpoints requieren
 * autenticacion: solo un usuario logueado ve la informacion no sensible de los
 * demas.
 */
@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    /** Servicio de usuarios. */
    private final UsuarioService usuarioService;

    /**
     * Crea el controlador.
     *
     * @param usuarioService el servicio de usuarios
     */
    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    /**
     * Lista los demas usuarios registrados (solo informacion publica); con
     * ?universidadId=N filtra por universidad.
     *
     * @param authentication el usuario autenticado
     * @param universidadId  la universidad a filtrar (opcional)
     * @return los usuarios del catalogo
     */
    @GetMapping
    public ResponseEntity<List<UsuarioPublicoDTO>> listarCatalogo(Authentication authentication,
            @RequestParam(required = false) Long universidadId) {
        return ResponseEntity.ok(usuarioService.listarCatalogo(authentication.getName(), universidadId));
    }

    /**
     * Ranking de usuarios con mas me gusta (por defecto los 20 primeros); con
     * ?universidadId=N solo los de esa universidad.
     *
     * @param authentication el usuario autenticado
     * @param universidadId  la universidad a filtrar (opcional)
     * @param limite         cuantos usuarios devolver (por defecto 20)
     * @return los usuarios del ranking
     */
    @GetMapping("/ranking")
    public ResponseEntity<List<UsuarioPublicoDTO>> ranking(Authentication authentication,
            @RequestParam(required = false) Long universidadId, @RequestParam(defaultValue = "20") int limite) {
        return ResponseEntity.ok(usuarioService.rankingMeGusta(authentication.getName(), universidadId, limite));
    }

    /**
     * Obtiene la informacion publica de un usuario por id.
     *
     * @param authentication el usuario autenticado
     * @param id             el identificador del usuario
     * @return la informacion publica del usuario
     */
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioPublicoDTO> obtenerPerfilPublico(Authentication authentication,
            @PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.obtenerPerfilPublico(authentication.getName(), id));
    }

    /**
     * Obtiene el perfil completo del usuario autenticado.
     *
     * @param authentication el usuario autenticado
     * @return su perfil completo
     */
    @GetMapping("/me")
    public ResponseEntity<UsuarioPerfilDTO> obtenerPerfilPropio(Authentication authentication) {
        return ResponseEntity.ok(usuarioService.obtenerPerfilPropio(authentication.getName()));
    }

    /**
     * Actualiza el perfil del usuario autenticado.
     *
     * @param authentication el usuario autenticado
     * @param request        los datos nuevos
     * @return su perfil actualizado
     */
    @PutMapping("/me")
    public ResponseEntity<UsuarioPerfilDTO> actualizarPerfilPropio(Authentication authentication,
            @Valid @RequestBody ActualizarPerfilRequestDTO request) {
        return ResponseEntity.ok(usuarioService.actualizarPerfilPropio(authentication.getName(), request));
    }

    /**
     * Elimina la cuenta del usuario autenticado.
     *
     * @param authentication el usuario autenticado
     * @return respuesta vacia (204)
     */
    @DeleteMapping("/me")
    public ResponseEntity<Void> eliminarCuentaPropia(Authentication authentication) {
        usuarioService.eliminarCuentaPropia(authentication.getName());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
