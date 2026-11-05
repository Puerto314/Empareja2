package co.edu.unbosque.empareja2.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.edu.unbosque.empareja2.dto.CarreraDTO;
import co.edu.unbosque.empareja2.dto.CiudadDTO;
import co.edu.unbosque.empareja2.dto.OfreceDTO;
import co.edu.unbosque.empareja2.dto.RedSocialDTO;
import co.edu.unbosque.empareja2.dto.UniversidadDTO;
import co.edu.unbosque.empareja2.service.CatalogoService;

/**
 * Datos de referencia de consulta PUBLICA (el formulario de registro los
 * necesita antes de que exista una sesion): ciudades, universidades, carreras,
 * redes sociales y oferta academica.
 */
@RestController
@RequestMapping("/api/catalogos")
public class CatalogoController {

    /** Servicio de los datos de referencia. */
    private final CatalogoService catalogoService;

    /**
     * Crea el controlador.
     *
     * @param catalogoService el servicio de los datos de referencia
     */
    public CatalogoController(CatalogoService catalogoService) {
        this.catalogoService = catalogoService;
    }

    /**
     * Lista las ciudades.
     *
     * @return las ciudades
     */
    @GetMapping("/ciudades")
    public ResponseEntity<List<CiudadDTO>> ciudades() {
        return ResponseEntity.ok(catalogoService.listarCiudades());
    }

    /**
     * Lista las universidades; con ?nombre=texto busca por nombre o sigla.
     *
     * @param nombre el texto a buscar (opcional)
     * @return las universidades
     */
    @GetMapping("/universidades")
    public ResponseEntity<List<UniversidadDTO>> universidades(@RequestParam(required = false) String nombre) {
        return ResponseEntity.ok(catalogoService.listarUniversidades(nombre));
    }

    /**
     * Lista las carreras.
     *
     * @return las carreras
     */
    @GetMapping("/carreras")
    public ResponseEntity<List<CarreraDTO>> carreras() {
        return ResponseEntity.ok(catalogoService.listarCarreras());
    }

    /**
     * Lista las redes sociales disponibles.
     *
     * @return las redes sociales
     */
    @GetMapping("/redes-sociales")
    public ResponseEntity<List<RedSocialDTO>> redesSociales() {
        return ResponseEntity.ok(catalogoService.listarRedesSociales());
    }

    /**
     * Lista que carreras ofrece cada universidad; con ?universidadId=N filtra por
     * universidad.
     *
     * @param universidadId la universidad a filtrar (opcional)
     * @return las ofertas
     */
    @GetMapping("/oferta")
    public ResponseEntity<List<OfreceDTO>> oferta(@RequestParam(required = false) Long universidadId) {
        return ResponseEntity.ok(catalogoService.listarOferta(universidadId));
    }

    /**
     * Obtiene una ciudad por id.
     *
     * @param id el identificador de la ciudad
     * @return la ciudad
     */
    @GetMapping("/ciudades/{id}")
    public ResponseEntity<CiudadDTO> ciudad(@PathVariable Long id) {
        return ResponseEntity.ok(catalogoService.obtenerCiudad(id));
    }

    /**
     * Obtiene una universidad por id.
     *
     * @param id el identificador de la universidad
     * @return la universidad
     */
    @GetMapping("/universidades/{id}")
    public ResponseEntity<UniversidadDTO> universidad(@PathVariable Long id) {
        return ResponseEntity.ok(catalogoService.obtenerUniversidad(id));
    }

    /**
     * Obtiene una carrera por id.
     *
     * @param id el identificador de la carrera
     * @return la carrera
     */
    @GetMapping("/carreras/{id}")
    public ResponseEntity<CarreraDTO> carrera(@PathVariable Long id) {
        return ResponseEntity.ok(catalogoService.obtenerCarrera(id));
    }

    /**
     * Obtiene una red social por id.
     *
     * @param id el identificador de la red social
     * @return la red social
     */
    @GetMapping("/redes-sociales/{id}")
    public ResponseEntity<RedSocialDTO> redSocial(@PathVariable Long id) {
        return ResponseEntity.ok(catalogoService.obtenerRedSocial(id));
    }
}
