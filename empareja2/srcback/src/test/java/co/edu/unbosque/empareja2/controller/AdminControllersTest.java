package co.edu.unbosque.empareja2.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import co.edu.unbosque.empareja2.dto.request.ActualizarOfertaRequestDTO;
import co.edu.unbosque.empareja2.dto.request.CiudadRequestDTO;
import co.edu.unbosque.empareja2.dto.response.CiudadDTO;
import co.edu.unbosque.empareja2.dto.response.OfreceDTO;
import co.edu.unbosque.empareja2.dto.response.ReporteDTO;
import co.edu.unbosque.empareja2.entity.EstadoReporte;
import co.edu.unbosque.empareja2.entity.TipoReporte;
import co.edu.unbosque.empareja2.exception.OperationNotAllowedException;
import co.edu.unbosque.empareja2.exception.ResourceNotFoundException;
import co.edu.unbosque.empareja2.service.CatalogoService;
import co.edu.unbosque.empareja2.service.ComentarioService;
import co.edu.unbosque.empareja2.service.ReaccionService;
import co.edu.unbosque.empareja2.service.ReporteService;
import co.edu.unbosque.empareja2.service.UsuarioService;

/**
 * Rutas, validacion y codigos HTTP del CRUD de administrador (la restriccion al
 * rol ADMIN la aplica SecurityConfig y no se prueba aqui).
 */
@ExtendWith(MockitoExtension.class)
class AdminControllersTest extends ControllerTestSupport {

    @Mock
    private UsuarioService usuarioService;
    @Mock
    private CatalogoService catalogoService;
    @Mock
    private ReporteService reporteService;
    @Mock
    private ComentarioService comentarioService;
    @Mock
    private ReaccionService reaccionService;

    private MockMvc mvc;

    @BeforeEach
    void preparar() {
        mvc = montar(new AdminController(usuarioService), new AdminCatalogoController(catalogoService),
                new AdminModeracionController(reporteService, comentarioService, reaccionService));
    }

    // ---- usuarios ----

    @Test
    void eliminarUltimoAdministradorDa409() throws Exception {
        org.mockito.Mockito.doThrow(new OperationNotAllowedException("ultimo admin")).when(usuarioService)
                .eliminarPorAdmin(9L);

        mvc.perform(delete("/api/admin/usuarios/9")).andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("ultimo admin"));
    }

    @Test
    void eliminarUsuarioDa204() throws Exception {
        mvc.perform(delete("/api/admin/usuarios/2")).andExpect(status().isNoContent());
        verify(usuarioService).eliminarPorAdmin(2L);
    }

    // ---- catalogos ----

    @Test
    void editarCiudadDa200() throws Exception {
        when(catalogoService.actualizarCiudad(eq(1L), any(CiudadRequestDTO.class)))
                .thenReturn(new CiudadDTO(1L, "Bogota", "Cundinamarca"));

        mvc.perform(put("/api/admin/catalogos/ciudades/1").contentType(MediaType.APPLICATION_JSON)
                .content(json.writeValueAsString(new CiudadRequestDTO("Bogota", "Cundinamarca"))))
                .andExpect(status().isOk()).andExpect(jsonPath("$.nombre").value("Bogota"));
    }

    @Test
    void editarCiudadInvalidaDa400() throws Exception {
        mvc.perform(put("/api/admin/catalogos/ciudades/1").contentType(MediaType.APPLICATION_JSON)
                .content(json.writeValueAsString(new CiudadRequestDTO(" ", "Cundinamarca"))))
                .andExpect(status().isBadRequest());
        verify(catalogoService, never()).actualizarCiudad(any(), any());
    }

    @Test
    void eliminarCiudadConUniversidadesDa409YLibreDa204() throws Exception {
        org.mockito.Mockito.doThrow(new OperationNotAllowedException("tiene universidades")).when(catalogoService)
                .eliminarCiudad(1L);

        mvc.perform(delete("/api/admin/catalogos/ciudades/1")).andExpect(status().isConflict());
        mvc.perform(delete("/api/admin/catalogos/ciudades/2")).andExpect(status().isNoContent());
        verify(catalogoService).eliminarCiudad(2L);
    }

    @Test
    void eliminarUniversidadInexistenteDa404() throws Exception {
        org.mockito.Mockito.doThrow(new ResourceNotFoundException("no existe")).when(catalogoService)
                .eliminarUniversidad(99L);

        mvc.perform(delete("/api/admin/catalogos/universidades/99")).andExpect(status().isNotFound());
    }

    @Test
    void editarYEliminarOfertaUsanLaLlaveCompuesta() throws Exception {
        when(catalogoService.actualizarOferta(eq(1L), eq(10L), any(ActualizarOfertaRequestDTO.class)))
                .thenReturn(new OfreceDTO(1L, "Medicina", 10L, "Uni", "Salud"));

        mvc.perform(put("/api/admin/catalogos/oferta/1/10").contentType(MediaType.APPLICATION_JSON)
                .content(json.writeValueAsString(new ActualizarOfertaRequestDTO("Salud"))))
                .andExpect(status().isOk()).andExpect(jsonPath("$.facultad").value("Salud"));
        mvc.perform(put("/api/admin/catalogos/oferta/1/10").contentType(MediaType.APPLICATION_JSON)
                .content("{\"facultad\":\"\"}")).andExpect(status().isBadRequest());

        mvc.perform(delete("/api/admin/catalogos/oferta/1/10")).andExpect(status().isNoContent());
        verify(catalogoService).eliminarOferta(1L, 10L);
    }

    // ---- moderacion ----

    private ReporteDTO reporte(EstadoReporte estado) {
        return new ReporteDTO(5L, 1L, "Ana Garcia", 2L, "Luis Perez", TipoReporte.ACOSO, estado, "x",
                LocalDateTime.now());
    }

    @Test
    void listarReportesSinFiltroYConFiltroDeEstado() throws Exception {
        when(reporteService.listarTodos()).thenReturn(List.of(reporte(EstadoReporte.PENDIENTE)));
        when(reporteService.listarPorEstado(EstadoReporte.RESUELTO)).thenReturn(List.of());

        mvc.perform(get("/api/admin/reportes")).andExpect(status().isOk())
                .andExpect(jsonPath("0.estado").value("PENDIENTE"));
        mvc.perform(get("/api/admin/reportes").param("estado", "RESUELTO")).andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
        mvc.perform(get("/api/admin/reportes").param("estado", "INVENTADO")).andExpect(status().isBadRequest());
    }

    @Test
    void cambiarEstadoDeUnReporte() throws Exception {
        when(reporteService.actualizarEstado(5L, EstadoReporte.RESUELTO))
                .thenReturn(reporte(EstadoReporte.RESUELTO));

        mvc.perform(put("/api/admin/reportes/5").contentType(MediaType.APPLICATION_JSON)
                .content("{\"estado\":\"RESUELTO\"}")).andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("RESUELTO"));
        mvc.perform(put("/api/admin/reportes/5").contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void eliminarReporteComentarioYMeGustaDa204() throws Exception {
        mvc.perform(delete("/api/admin/reportes/5")).andExpect(status().isNoContent());
        mvc.perform(delete("/api/admin/comentarios/1/3")).andExpect(status().isNoContent());
        mvc.perform(delete("/api/admin/me-gusta/1/2")).andExpect(status().isNoContent());

        verify(reporteService).eliminar(5L);
        verify(comentarioService).eliminarPorAdmin(1L, 3);
        verify(reaccionService).eliminarPorAdmin(1L, 2);
    }

    @Test
    void listarComentariosYMeGustaDelSistema() throws Exception {
        mvc.perform(get("/api/admin/comentarios")).andExpect(status().isOk());
        mvc.perform(get("/api/admin/me-gusta")).andExpect(status().isOk());

        verify(comentarioService).listarTodosParaAdmin();
        verify(reaccionService).listarTodasParaAdmin();
    }

    @Test
    void rutaInexistenteYaNoDa500() throws Exception {
        mvc.perform(get("/api/admin/no-existe")).andExpect(status().isNotFound());
    }
}
