package co.edu.unbosque.empareja2.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.unbosque.empareja2.dto.request.ActualizarOfertaRequestDTO;
import co.edu.unbosque.empareja2.dto.request.CarreraRequestDTO;
import co.edu.unbosque.empareja2.dto.request.CiudadRequestDTO;
import co.edu.unbosque.empareja2.dto.request.OfreceRequestDTO;
import co.edu.unbosque.empareja2.dto.request.RedSocialRequestDTO;
import co.edu.unbosque.empareja2.dto.request.UniversidadRequestDTO;
import co.edu.unbosque.empareja2.dto.response.CarreraDTO;
import co.edu.unbosque.empareja2.dto.response.CiudadDTO;
import co.edu.unbosque.empareja2.dto.response.OfreceDTO;
import co.edu.unbosque.empareja2.dto.response.RedSocialDTO;
import co.edu.unbosque.empareja2.dto.response.UniversidadDTO;
import co.edu.unbosque.empareja2.service.CatalogoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

/**
 * CRUD de los datos de referencia (ciudades, universidades, carreras, redes
 * sociales y oferta academica). La LECTURA es publica (CatalogoController);
 * crear, editar y eliminar es solo para ADMIN.
 */
@RestController
@RequestMapping("/api/admin/catalogos")
@Tag(name = "Administracion - Catalogos", description = "Crear, editar y eliminar datos de referencia (solo ADMIN)")
public class AdminCatalogoController {

    private final CatalogoService catalogoService;

    public AdminCatalogoController(CatalogoService catalogoService) {
        this.catalogoService = catalogoService;
    }

    // ---- Ciudades ----

    @PostMapping("/ciudades")
    @Operation(summary = "Crea una ciudad")
    public ResponseEntity<CiudadDTO> crearCiudad(@Valid @RequestBody CiudadRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(catalogoService.crearCiudad(request));
    }

    @PutMapping("/ciudades/{id}")
    @Operation(summary = "Edita una ciudad")
    public ResponseEntity<CiudadDTO> actualizarCiudad(@PathVariable Long id,
            @Valid @RequestBody CiudadRequestDTO request) {
        return ResponseEntity.ok(catalogoService.actualizarCiudad(id, request));
    }

    @DeleteMapping("/ciudades/{id}")
    @Operation(summary = "Elimina una ciudad (409 si todavia tiene universidades)")
    public ResponseEntity<Void> eliminarCiudad(@PathVariable Long id) {
        catalogoService.eliminarCiudad(id);
        return ResponseEntity.noContent().build();
    }

    // ---- Universidades ----

    @PostMapping("/universidades")
    @Operation(summary = "Crea una universidad")
    public ResponseEntity<UniversidadDTO> crearUniversidad(@Valid @RequestBody UniversidadRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(catalogoService.crearUniversidad(request));
    }

    @PutMapping("/universidades/{id}")
    @Operation(summary = "Edita una universidad")
    public ResponseEntity<UniversidadDTO> actualizarUniversidad(@PathVariable Long id,
            @Valid @RequestBody UniversidadRequestDTO request) {
        return ResponseEntity.ok(catalogoService.actualizarUniversidad(id, request));
    }

    @DeleteMapping("/universidades/{id}")
    @Operation(summary = "Elimina una universidad y su oferta (409 si hay estudiantes en ella)")
    public ResponseEntity<Void> eliminarUniversidad(@PathVariable Long id) {
        catalogoService.eliminarUniversidad(id);
        return ResponseEntity.noContent().build();
    }

    // ---- Carreras ----

    @PostMapping("/carreras")
    @Operation(summary = "Crea una carrera")
    public ResponseEntity<CarreraDTO> crearCarrera(@Valid @RequestBody CarreraRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(catalogoService.crearCarrera(request));
    }

    @PutMapping("/carreras/{id}")
    @Operation(summary = "Edita una carrera")
    public ResponseEntity<CarreraDTO> actualizarCarrera(@PathVariable Long id,
            @Valid @RequestBody CarreraRequestDTO request) {
        return ResponseEntity.ok(catalogoService.actualizarCarrera(id, request));
    }

    @DeleteMapping("/carreras/{id}")
    @Operation(summary = "Elimina una carrera y su oferta (409 si hay estudiantes que la cursan)")
    public ResponseEntity<Void> eliminarCarrera(@PathVariable Long id) {
        catalogoService.eliminarCarrera(id);
        return ResponseEntity.noContent().build();
    }

    // ---- Redes sociales ----

    @PostMapping("/redes-sociales")
    @Operation(summary = "Crea una red social")
    public ResponseEntity<RedSocialDTO> crearRedSocial(@Valid @RequestBody RedSocialRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(catalogoService.crearRedSocial(request));
    }

    @PutMapping("/redes-sociales/{id}")
    @Operation(summary = "Edita una red social")
    public ResponseEntity<RedSocialDTO> actualizarRedSocial(@PathVariable Long id,
            @Valid @RequestBody RedSocialRequestDTO request) {
        return ResponseEntity.ok(catalogoService.actualizarRedSocial(id, request));
    }

    @DeleteMapping("/redes-sociales/{id}")
    @Operation(summary = "Elimina una red social (409 si algun usuario la tiene en su perfil)")
    public ResponseEntity<Void> eliminarRedSocial(@PathVariable Long id) {
        catalogoService.eliminarRedSocial(id);
        return ResponseEntity.noContent().build();
    }

    // ---- Oferta academica (universidad - carrera - facultad) ----

    @PostMapping("/oferta")
    @Operation(summary = "Registra que una universidad ofrece una carrera (con su facultad)")
    public ResponseEntity<OfreceDTO> crearOferta(@Valid @RequestBody OfreceRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(catalogoService.crearOferta(request));
    }

    @PutMapping("/oferta/{idCarrera}/{idUniversidad}")
    @Operation(summary = "Corrige la facultad de una oferta")
    public ResponseEntity<OfreceDTO> actualizarOferta(@PathVariable Long idCarrera, @PathVariable Long idUniversidad,
            @Valid @RequestBody ActualizarOfertaRequestDTO request) {
        return ResponseEntity.ok(catalogoService.actualizarOferta(idCarrera, idUniversidad, request));
    }

    @DeleteMapping("/oferta/{idCarrera}/{idUniversidad}")
    @Operation(summary = "Elimina una oferta (409 si hay estudiantes que cursan esa carrera ahi)")
    public ResponseEntity<Void> eliminarOferta(@PathVariable Long idCarrera, @PathVariable Long idUniversidad) {
        catalogoService.eliminarOferta(idCarrera, idUniversidad);
        return ResponseEntity.noContent().build();
    }
}
