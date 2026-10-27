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
import co.edu.unbosque.empareja2.dto.request.CarreraRequestDTO;
import co.edu.unbosque.empareja2.dto.request.CiudadRequestDTO;
import co.edu.unbosque.empareja2.dto.request.OfreceRequestDTO;
import co.edu.unbosque.empareja2.dto.request.RedSocialRequestDTO;
import co.edu.unbosque.empareja2.dto.request.UniversidadRequestDTO;
import co.edu.unbosque.empareja2.dto.response.CarreraDTO;
import co.edu.unbosque.empareja2.dto.response.CiudadDTO;
import co.edu.unbosque.empareja2.dto.response.OfreceDTO;
import co.edu.unbosque.empareja2.dto.response.RedSocialDTO;
import co.edu.unbosque.empareja2.dto.response.ReporteDTO;
import co.edu.unbosque.empareja2.dto.response.UniversidadDTO;
import co.edu.unbosque.empareja2.dto.response.UsuarioPerfilDTO;
import co.edu.unbosque.empareja2.service.CatalogoService;
import co.edu.unbosque.empareja2.service.ReporteService;
import co.edu.unbosque.empareja2.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

/**
 * Administracion: CRUD de usuarios, consulta de reportes y alta de datos de
 * referencia. Todo "/api/admin/**" esta restringido al rol ADMIN en
 * SecurityConfig.
 */
@RestController
@RequestMapping("/api/admin")
@Tag(name = "Administracion", description = "Usuarios, reportes y catalogos (solo ADMIN)")
public class AdminController {

    private final UsuarioService usuarioService;
    private final ReporteService reporteService;
    private final CatalogoService catalogoService;

    public AdminController(UsuarioService usuarioService, ReporteService reporteService,
            CatalogoService catalogoService) {
        this.usuarioService = usuarioService;
        this.reporteService = reporteService;
        this.catalogoService = catalogoService;
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

    // ---- Reportes ----

    @GetMapping("/reportes")
    @Operation(summary = "Lista todos los reportes de perfiles, del mas reciente al mas antiguo")
    public ResponseEntity<List<ReporteDTO>> listarReportes() {
        return ResponseEntity.ok(reporteService.listarTodos());
    }

    // ---- Datos de referencia ----

    @PostMapping("/catalogos/ciudades")
    @Operation(summary = "Crea una ciudad")
    public ResponseEntity<CiudadDTO> crearCiudad(@Valid @RequestBody CiudadRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(catalogoService.crearCiudad(request));
    }

    @PostMapping("/catalogos/universidades")
    @Operation(summary = "Crea una universidad")
    public ResponseEntity<UniversidadDTO> crearUniversidad(@Valid @RequestBody UniversidadRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(catalogoService.crearUniversidad(request));
    }

    @PostMapping("/catalogos/carreras")
    @Operation(summary = "Crea una carrera")
    public ResponseEntity<CarreraDTO> crearCarrera(@Valid @RequestBody CarreraRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(catalogoService.crearCarrera(request));
    }

    @PostMapping("/catalogos/redes-sociales")
    @Operation(summary = "Crea una red social")
    public ResponseEntity<RedSocialDTO> crearRedSocial(@Valid @RequestBody RedSocialRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(catalogoService.crearRedSocial(request));
    }

    @PostMapping("/catalogos/oferta")
    @Operation(summary = "Registra que una universidad ofrece una carrera (con su facultad)")
    public ResponseEntity<OfreceDTO> crearOferta(@Valid @RequestBody OfreceRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(catalogoService.crearOferta(request));
    }
}
