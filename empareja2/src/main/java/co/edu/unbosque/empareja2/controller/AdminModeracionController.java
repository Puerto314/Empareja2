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

import co.edu.unbosque.empareja2.dto.ActualizarReporteRequestDTO;
import co.edu.unbosque.empareja2.dto.ComentarioDTO;
import co.edu.unbosque.empareja2.dto.ReaccionAdminDTO;
import co.edu.unbosque.empareja2.dto.ReporteDTO;
import co.edu.unbosque.empareja2.entity.EstadoReporte;
import co.edu.unbosque.empareja2.service.ComentarioService;
import co.edu.unbosque.empareja2.service.ReaccionService;
import co.edu.unbosque.empareja2.service.ReporteService;
import jakarta.validation.Valid;

/**
 * Moderacion: el administrador revisa los reportes (y cambia su estado) y puede
 * consultar y eliminar comentarios y "me gusta". No crea comentarios, reacciones
 * ni reportes a nombre de otros usuarios: esas interacciones solo las generan los
 * propios usuarios.
 */
@RestController
@RequestMapping("/api/admin")
public class AdminModeracionController {

    /** Servicio de reportes. */
    private final ReporteService reporteService;

    /** Servicio de comentarios. */
    private final ComentarioService comentarioService;

    /** Servicio de me gusta. */
    private final ReaccionService reaccionService;

    /**
     * Crea el controlador.
     *
     * @param reporteService    el servicio de reportes
     * @param comentarioService el servicio de comentarios
     * @param reaccionService   el servicio de me gusta
     */
    public AdminModeracionController(ReporteService reporteService, ComentarioService comentarioService,
            ReaccionService reaccionService) {
        this.reporteService = reporteService;
        this.comentarioService = comentarioService;
        this.reaccionService = reaccionService;
    }

    /**
     * Lista los reportes, del mas reciente al mas antiguo; con ?estado=PENDIENTE
     * filtra por estado.
     *
     * @param estado el estado a filtrar (opcional)
     * @return los reportes
     */
    @GetMapping("/reportes")
    public ResponseEntity<List<ReporteDTO>> listarReportes(@RequestParam(required = false) EstadoReporte estado) {
        if (estado == null) {
            return ResponseEntity.ok(reporteService.listarTodos());
        }
        return ResponseEntity.ok(reporteService.listarPorEstado(estado));
    }

    /**
     * Obtiene un reporte.
     *
     * @param id el identificador del reporte
     * @return el reporte
     */
    @GetMapping("/reportes/{id}")
    public ResponseEntity<ReporteDTO> obtenerReporte(@PathVariable Long id) {
        return ResponseEntity.ok(reporteService.obtener(id));
    }

    /**
     * Cambia el estado de un reporte (PENDIENTE, EN_REVISION, RESUELTO, DESCARTADO).
     *
     * @param id      el identificador del reporte
     * @param request el estado nuevo
     * @return el reporte actualizado
     */
    @PutMapping("/reportes/{id}")
    public ResponseEntity<ReporteDTO> actualizarReporte(@PathVariable Long id,
            @Valid @RequestBody ActualizarReporteRequestDTO request) {
        return ResponseEntity.ok(reporteService.actualizarEstado(id, request.getEstado()));
    }

    /**
     * Elimina un reporte.
     *
     * @param id el identificador del reporte
     * @return respuesta vacia (204)
     */
    @DeleteMapping("/reportes/{id}")
    public ResponseEntity<Void> eliminarReporte(@PathVariable Long id) {
        reporteService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Lista todos los comentarios privados del sistema.
     *
     * @return los comentarios
     */
    @GetMapping("/comentarios")
    public ResponseEntity<List<ComentarioDTO>> listarComentarios() {
        return ResponseEntity.ok(comentarioService.listarTodosParaAdmin());
    }

    /**
     * Elimina un comentario (se identifica por autor + numero).
     *
     * @param idAutor          el identificador del autor
     * @param numeroComentario el numero del comentario
     * @return respuesta vacia (204)
     */
    @DeleteMapping("/comentarios/{idAutor}/{numeroComentario}")
    public ResponseEntity<Void> eliminarComentario(@PathVariable Long idAutor, @PathVariable Integer numeroComentario) {
        comentarioService.eliminarPorAdmin(idAutor, numeroComentario);
        return ResponseEntity.noContent().build();
    }

    /**
     * Lista todos los me gusta del sistema.
     *
     * @return los me gusta
     */
    @GetMapping("/me-gusta")
    public ResponseEntity<List<ReaccionAdminDTO>> listarMeGusta() {
        return ResponseEntity.ok(reaccionService.listarTodasParaAdmin());
    }

    /**
     * Elimina un me gusta (se identifica por autor + numero).
     *
     * @param idAutor        el identificador del autor
     * @param numeroReaccion el numero de la reaccion
     * @return respuesta vacia (204)
     */
    @DeleteMapping("/me-gusta/{idAutor}/{numeroReaccion}")
    public ResponseEntity<Void> eliminarMeGusta(@PathVariable Long idAutor, @PathVariable Integer numeroReaccion) {
        reaccionService.eliminarPorAdmin(idAutor, numeroReaccion);
        return ResponseEntity.noContent().build();
    }
}
