package co.edu.unbosque.empareja2.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.edu.unbosque.empareja2.dto.request.ActualizarReporteRequestDTO;
import co.edu.unbosque.empareja2.dto.response.ComentarioDTO;
import co.edu.unbosque.empareja2.dto.response.ReaccionAdminDTO;
import co.edu.unbosque.empareja2.dto.response.ReporteDTO;
import co.edu.unbosque.empareja2.entity.EstadoReporte;
import co.edu.unbosque.empareja2.service.ComentarioService;
import co.edu.unbosque.empareja2.service.ReaccionService;
import co.edu.unbosque.empareja2.service.ReporteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

/**
 * Moderacion: el administrador revisa los reportes (y cambia su estado) y puede
 * consultar y eliminar comentarios y "me gusta". No crea comentarios, reacciones
 * ni reportes a nombre de otros usuarios: esas interacciones solo las generan los
 * propios usuarios.
 */
@RestController
@RequestMapping("/api/admin")
@Tag(name = "Administracion - Moderacion", description = "Reportes, comentarios y me gusta (solo ADMIN)")
public class AdminModeracionController {

    private final ReporteService reporteService;
    private final ComentarioService comentarioService;
    private final ReaccionService reaccionService;

    public AdminModeracionController(ReporteService reporteService, ComentarioService comentarioService,
            ReaccionService reaccionService) {
        this.reporteService = reporteService;
        this.comentarioService = comentarioService;
        this.reaccionService = reaccionService;
    }

    // ---- Reportes ----

    @GetMapping("/reportes")
    @Operation(summary = "Lista los reportes, del mas reciente al mas antiguo; con ?estado=PENDIENTE filtra por estado")
    public ResponseEntity<List<ReporteDTO>> listarReportes(@RequestParam(required = false) EstadoReporte estado) {
        return ResponseEntity.ok(estado == null ? reporteService.listarTodos() : reporteService.listarPorEstado(estado));
    }

    @GetMapping("/reportes/{id}")
    @Operation(summary = "Obtiene un reporte")
    public ResponseEntity<ReporteDTO> obtenerReporte(@PathVariable Long id) {
        return ResponseEntity.ok(reporteService.obtener(id));
    }

    @PutMapping("/reportes/{id}")
    @Operation(summary = "Cambia el estado de un reporte (PENDIENTE, EN_REVISION, RESUELTO, DESCARTADO)")
    public ResponseEntity<ReporteDTO> actualizarReporte(@PathVariable Long id,
            @Valid @RequestBody ActualizarReporteRequestDTO request) {
        return ResponseEntity.ok(reporteService.actualizarEstado(id, request.estado()));
    }

    @DeleteMapping("/reportes/{id}")
    @Operation(summary = "Elimina un reporte")
    public ResponseEntity<Void> eliminarReporte(@PathVariable Long id) {
        reporteService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    // ---- Comentarios ----

    @GetMapping("/comentarios")
    @Operation(summary = "Lista todos los comentarios privados del sistema")
    public ResponseEntity<List<ComentarioDTO>> listarComentarios() {
        return ResponseEntity.ok(comentarioService.listarTodosParaAdmin());
    }

    @DeleteMapping("/comentarios/{idAutor}/{numeroComentario}")
    @Operation(summary = "Elimina un comentario (se identifica por autor + numero)")
    public ResponseEntity<Void> eliminarComentario(@PathVariable Long idAutor, @PathVariable Integer numeroComentario) {
        comentarioService.eliminarPorAdmin(idAutor, numeroComentario);
        return ResponseEntity.noContent().build();
    }

    // ---- Me gusta ----

    @GetMapping("/me-gusta")
    @Operation(summary = "Lista todos los me gusta del sistema")
    public ResponseEntity<List<ReaccionAdminDTO>> listarMeGusta() {
        return ResponseEntity.ok(reaccionService.listarTodasParaAdmin());
    }

    @DeleteMapping("/me-gusta/{idAutor}/{numeroReaccion}")
    @Operation(summary = "Elimina un me gusta (se identifica por autor + numero)")
    public ResponseEntity<Void> eliminarMeGusta(@PathVariable Long idAutor, @PathVariable Integer numeroReaccion) {
        reaccionService.eliminarPorAdmin(idAutor, numeroReaccion);
        return ResponseEntity.noContent().build();
    }
}
