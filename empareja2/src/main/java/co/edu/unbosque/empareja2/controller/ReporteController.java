package co.edu.unbosque.empareja2.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.unbosque.empareja2.dto.ReporteDTO;
import co.edu.unbosque.empareja2.dto.ReporteRequestDTO;
import co.edu.unbosque.empareja2.service.ReporteService;
import jakarta.validation.Valid;

/**
 * Reportar un perfil. La consulta de reportes es solo para administradores
 * (ver AdminModeracionController).
 */
@RestController
@RequestMapping("/api/usuarios/{id}/reportes")
public class ReporteController {

    /** Servicio de reportes. */
    private final ReporteService reporteService;

    /**
     * Crea el controlador.
     *
     * @param reporteService el servicio de reportes
     */
    public ReporteController(ReporteService reporteService) {
        this.reporteService = reporteService;
    }

    /**
     * Reporta el perfil indicado.
     *
     * @param authentication el usuario autenticado
     * @param id             el identificador del perfil reportado
     * @param request        el motivo y la descripcion
     * @return el reporte creado (201)
     */
    @PostMapping
    public ResponseEntity<ReporteDTO> reportar(Authentication authentication, @PathVariable Long id,
            @Valid @RequestBody ReporteRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(reporteService.reportar(authentication.getName(), id, request));
    }
}
