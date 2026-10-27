package co.edu.unbosque.empareja2.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.edu.unbosque.empareja2.dto.response.CarreraDTO;
import co.edu.unbosque.empareja2.dto.response.CiudadDTO;
import co.edu.unbosque.empareja2.dto.response.OfreceDTO;
import co.edu.unbosque.empareja2.dto.response.RedSocialDTO;
import co.edu.unbosque.empareja2.dto.response.UniversidadDTO;
import co.edu.unbosque.empareja2.service.CatalogoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Datos de referencia de consulta PUBLICA (el formulario de registro los
 * necesita antes de que exista una sesion).
 */
@RestController
@RequestMapping("/api/catalogos")
@Tag(name = "Catalogos", description = "Ciudades, universidades, carreras, redes sociales y oferta academica")
public class CatalogoController {

    private final CatalogoService catalogoService;

    public CatalogoController(CatalogoService catalogoService) {
        this.catalogoService = catalogoService;
    }

    @GetMapping("/ciudades")
    @Operation(summary = "Lista las ciudades")
    public ResponseEntity<List<CiudadDTO>> ciudades() {
        return ResponseEntity.ok(catalogoService.listarCiudades());
    }

    @GetMapping("/universidades")
    @Operation(summary = "Lista las universidades; con ?nombre=texto busca por nombre o sigla")
    public ResponseEntity<List<UniversidadDTO>> universidades(@RequestParam(required = false) String nombre) {
        return ResponseEntity.ok(catalogoService.listarUniversidades(nombre));
    }

    @GetMapping("/carreras")
    @Operation(summary = "Lista las carreras")
    public ResponseEntity<List<CarreraDTO>> carreras() {
        return ResponseEntity.ok(catalogoService.listarCarreras());
    }

    @GetMapping("/redes-sociales")
    @Operation(summary = "Lista las redes sociales disponibles")
    public ResponseEntity<List<RedSocialDTO>> redesSociales() {
        return ResponseEntity.ok(catalogoService.listarRedesSociales());
    }

    @GetMapping("/oferta")
    @Operation(summary = "Lista que carreras ofrece cada universidad; con ?universidadId=N filtra por universidad")
    public ResponseEntity<List<OfreceDTO>> oferta(@RequestParam(required = false) Long universidadId) {
        return ResponseEntity.ok(catalogoService.listarOferta(universidadId));
    }
}
