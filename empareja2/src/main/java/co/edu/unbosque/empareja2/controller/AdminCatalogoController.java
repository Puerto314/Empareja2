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

import co.edu.unbosque.empareja2.dto.ActualizarOfertaRequestDTO;
import co.edu.unbosque.empareja2.dto.CarreraDTO;
import co.edu.unbosque.empareja2.dto.CarreraRequestDTO;
import co.edu.unbosque.empareja2.dto.CiudadDTO;
import co.edu.unbosque.empareja2.dto.CiudadRequestDTO;
import co.edu.unbosque.empareja2.dto.OfreceDTO;
import co.edu.unbosque.empareja2.dto.OfreceRequestDTO;
import co.edu.unbosque.empareja2.dto.UniversidadDTO;
import co.edu.unbosque.empareja2.dto.UniversidadRequestDTO;
import co.edu.unbosque.empareja2.service.CatalogoService;
import jakarta.validation.Valid;

/**
 * CRUD de los datos de referencia (ciudades, universidades, carreras y oferta
 * academica). La LECTURA es publica (CatalogoController); crear, editar y
 * eliminar es solo para ADMIN.
 */
@RestController
@RequestMapping("/api/admin/catalogos")
public class AdminCatalogoController {

    /** Servicio de los datos de referencia. */
    private final CatalogoService catalogoService;

    /**
     * Crea el controlador.
     *
     * @param catalogoService el servicio de los datos de referencia
     */
    public AdminCatalogoController(CatalogoService catalogoService) {
        this.catalogoService = catalogoService;
    }

    // ---- Ciudades ----

    /**
     * Crea una ciudad.
     *
     * @param request los datos de la ciudad
     * @return la ciudad creada (201)
     */
    @PostMapping("/ciudades")
    public ResponseEntity<CiudadDTO> crearCiudad(@Valid @RequestBody CiudadRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(catalogoService.crearCiudad(request));
    }

    /**
     * Edita una ciudad.
     *
     * @param id      el identificador de la ciudad
     * @param request los datos nuevos
     * @return la ciudad editada
     */
    @PutMapping("/ciudades/{id}")
    public ResponseEntity<CiudadDTO> actualizarCiudad(@PathVariable Long id,
            @Valid @RequestBody CiudadRequestDTO request) {
        return ResponseEntity.ok(catalogoService.actualizarCiudad(id, request));
    }

    /**
     * Elimina una ciudad (409 si todavia tiene universidades).
     *
     * @param id el identificador de la ciudad
     * @return respuesta vacia (204)
     */
    @DeleteMapping("/ciudades/{id}")
    public ResponseEntity<Void> eliminarCiudad(@PathVariable Long id) {
        catalogoService.eliminarCiudad(id);
        return ResponseEntity.noContent().build();
    }

    // ---- Universidades ----

    /**
     * Crea una universidad.
     *
     * @param request los datos de la universidad
     * @return la universidad creada (201)
     */
    @PostMapping("/universidades")
    public ResponseEntity<UniversidadDTO> crearUniversidad(@Valid @RequestBody UniversidadRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(catalogoService.crearUniversidad(request));
    }

    /**
     * Edita una universidad.
     *
     * @param id      el identificador de la universidad
     * @param request los datos nuevos
     * @return la universidad editada
     */
    @PutMapping("/universidades/{id}")
    public ResponseEntity<UniversidadDTO> actualizarUniversidad(@PathVariable Long id,
            @Valid @RequestBody UniversidadRequestDTO request) {
        return ResponseEntity.ok(catalogoService.actualizarUniversidad(id, request));
    }

    /**
     * Elimina una universidad y su oferta (409 si hay estudiantes en ella).
     *
     * @param id el identificador de la universidad
     * @return respuesta vacia (204)
     */
    @DeleteMapping("/universidades/{id}")
    public ResponseEntity<Void> eliminarUniversidad(@PathVariable Long id) {
        catalogoService.eliminarUniversidad(id);
        return ResponseEntity.noContent().build();
    }

    // ---- Carreras ----

    /**
     * Crea una carrera.
     *
     * @param request los datos de la carrera
     * @return la carrera creada (201)
     */
    @PostMapping("/carreras")
    public ResponseEntity<CarreraDTO> crearCarrera(@Valid @RequestBody CarreraRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(catalogoService.crearCarrera(request));
    }

    /**
     * Edita una carrera.
     *
     * @param id      el identificador de la carrera
     * @param request los datos nuevos
     * @return la carrera editada
     */
    @PutMapping("/carreras/{id}")
    public ResponseEntity<CarreraDTO> actualizarCarrera(@PathVariable Long id,
            @Valid @RequestBody CarreraRequestDTO request) {
        return ResponseEntity.ok(catalogoService.actualizarCarrera(id, request));
    }

    /**
     * Elimina una carrera y su oferta (409 si hay estudiantes que la cursan).
     *
     * @param id el identificador de la carrera
     * @return respuesta vacia (204)
     */
    @DeleteMapping("/carreras/{id}")
    public ResponseEntity<Void> eliminarCarrera(@PathVariable Long id) {
        catalogoService.eliminarCarrera(id);
        return ResponseEntity.noContent().build();
    }

    // ---- Oferta academica (universidad - carrera - facultad) ----

    /**
     * Registra que una universidad ofrece una carrera.
     *
     * @param request la carrera y la universidad
     * @return la oferta creada (201)
     */
    @PostMapping("/oferta")
    public ResponseEntity<OfreceDTO> crearOferta(@Valid @RequestBody OfreceRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(catalogoService.crearOferta(request));
    }

    /**
     * Corrige la facultad de la carrera de una oferta (la facultad es de la carrera).
     *
     * @param idCarrera     el identificador de la carrera
     * @param idUniversidad el identificador de la universidad
     * @param request       la facultad nueva
     * @return la oferta editada
     */
    @PutMapping("/oferta/{idCarrera}/{idUniversidad}")
    public ResponseEntity<OfreceDTO> actualizarOferta(@PathVariable Long idCarrera, @PathVariable Long idUniversidad,
            @Valid @RequestBody ActualizarOfertaRequestDTO request) {
        return ResponseEntity.ok(catalogoService.actualizarOferta(idCarrera, idUniversidad, request));
    }

    /**
     * Elimina una oferta (409 si hay estudiantes que cursan esa carrera ahi).
     *
     * @param idCarrera     el identificador de la carrera
     * @param idUniversidad el identificador de la universidad
     * @return respuesta vacia (204)
     */
    @DeleteMapping("/oferta/{idCarrera}/{idUniversidad}")
    public ResponseEntity<Void> eliminarOferta(@PathVariable Long idCarrera, @PathVariable Long idUniversidad) {
        catalogoService.eliminarOferta(idCarrera, idUniversidad);
        return ResponseEntity.noContent().build();
    }
}
