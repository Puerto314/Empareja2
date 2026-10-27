package co.edu.unbosque.empareja2.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

import co.edu.unbosque.empareja2.dto.request.ComentarioRequestDTO;
import co.edu.unbosque.empareja2.dto.request.ReporteRequestDTO;
import co.edu.unbosque.empareja2.dto.response.ComentarioDTO;
import co.edu.unbosque.empareja2.dto.response.MeGustaDTO;
import co.edu.unbosque.empareja2.dto.response.ReaccionDTO;
import co.edu.unbosque.empareja2.dto.response.ReporteDTO;
import co.edu.unbosque.empareja2.entity.TipoReaccion;
import co.edu.unbosque.empareja2.entity.TipoReporte;
import co.edu.unbosque.empareja2.exception.DuplicateResourceException;
import co.edu.unbosque.empareja2.service.ComentarioService;
import co.edu.unbosque.empareja2.service.ReaccionService;
import co.edu.unbosque.empareja2.service.ReporteService;

/**
 * Me gusta, comentarios privados y reportes.
 */
@ExtendWith(MockitoExtension.class)
class InteraccionControllersTest extends ControllerTestSupport {

    @Mock
    private ReaccionService reaccionService;
    @Mock
    private ComentarioService comentarioService;
    @Mock
    private ReporteService reporteService;

    private MockMvc mvc;

    @BeforeEach
    void preparar() {
        mvc = montar(new ReaccionController(reaccionService), new ComentarioController(comentarioService),
                new ReporteController(reporteService));
    }

    // ---- me gusta ----

    @Test
    void darMeGustaDa201() throws Exception {
        when(reaccionService.darMeGusta("ana@mail.com", 2L)).thenReturn(new MeGustaDTO(2L, 1L, true));

        mvc.perform(post("/api/usuarios/2/me-gusta").principal(ANA)).andExpect(status().isCreated())
                .andExpect(jsonPath("$.totalMeGusta").value(1)).andExpect(jsonPath("$.meGusta").value(true));
    }

    @Test
    void darMeGustaDosVecesDa409() throws Exception {
        when(reaccionService.darMeGusta("ana@mail.com", 2L)).thenThrow(new DuplicateResourceException("ya"));

        mvc.perform(post("/api/usuarios/2/me-gusta").principal(ANA)).andExpect(status().isConflict());
    }

    @Test
    void quitarMeGustaDa200() throws Exception {
        when(reaccionService.quitarMeGusta("ana@mail.com", 2L)).thenReturn(new MeGustaDTO(2L, 0L, false));

        mvc.perform(delete("/api/usuarios/2/me-gusta").principal(ANA)).andExpect(status().isOk())
                .andExpect(jsonPath("$.meGusta").value(false));
    }

    @Test
    void listarMeGustaEsPublico() throws Exception {
        when(reaccionService.listarMeGusta(2L)).thenReturn(
                List.of(new ReaccionDTO(1L, "Ana Garcia", null, TipoReaccion.ME_GUSTA, LocalDateTime.now())));

        mvc.perform(get("/api/usuarios/2/me-gusta").principal(ANA)).andExpect(status().isOk())
                .andExpect(jsonPath("0.nombreAutor").value("Ana Garcia"));
    }

    // ---- comentarios ----

    @Test
    void enviarComentarioPrivadoDa201() throws Exception {
        when(comentarioService.enviar(org.mockito.ArgumentMatchers.eq("ana@mail.com"),
                org.mockito.ArgumentMatchers.eq(2L), any(ComentarioRequestDTO.class)))
                .thenReturn(new ComentarioDTO(1, 1L, "Ana Garcia", 2L, "Luis Perez", "hola", LocalDateTime.now()));

        mvc.perform(post("/api/usuarios/2/comentarios").principal(ANA).contentType(MediaType.APPLICATION_JSON)
                .content(json.writeValueAsString(new ComentarioRequestDTO("hola")))).andExpect(status().isCreated())
                .andExpect(jsonPath("$.contenido").value("hola"));
    }

    @Test
    void comentarioVacioOMuyLargoDa400() throws Exception {
        mvc.perform(post("/api/usuarios/2/comentarios").principal(ANA).contentType(MediaType.APPLICATION_JSON)
                .content(json.writeValueAsString(new ComentarioRequestDTO("  ")))).andExpect(status().isBadRequest());
        mvc.perform(post("/api/usuarios/2/comentarios").principal(ANA).contentType(MediaType.APPLICATION_JSON)
                .content(json.writeValueAsString(new ComentarioRequestDTO("x".repeat(1001)))))
                .andExpect(status().isBadRequest());
        verify(comentarioService, never()).enviar(any(), any(), any());
    }

    @Test
    void bandejasSeConsultanSiempreConElUsuarioAutenticado() throws Exception {
        mvc.perform(get("/api/usuarios/me/comentarios/recibidos").principal(ANA)).andExpect(status().isOk());
        mvc.perform(get("/api/usuarios/me/comentarios/enviados").principal(ANA)).andExpect(status().isOk());

        verify(comentarioService).listarRecibidos("ana@mail.com");
        verify(comentarioService).listarEnviados("ana@mail.com");
    }

    // ---- reportes ----

    @Test
    void reportarPerfilDa201() throws Exception {
        when(reporteService.reportar(org.mockito.ArgumentMatchers.eq("ana@mail.com"),
                org.mockito.ArgumentMatchers.eq(2L), any(ReporteRequestDTO.class)))
                .thenReturn(new ReporteDTO(1L, 1L, "Ana Garcia", 2L, "Luis Perez", TipoReporte.SPAM, "spam",
                        LocalDateTime.now()));

        mvc.perform(post("/api/usuarios/2/reportes").principal(ANA).contentType(MediaType.APPLICATION_JSON)
                .content(json.writeValueAsString(new ReporteRequestDTO(TipoReporte.SPAM, "spam"))))
                .andExpect(status().isCreated()).andExpect(jsonPath("$.tipoReporte").value("SPAM"));
    }

    @Test
    void reporteSinTipoOConTipoDesconocidoDa400() throws Exception {
        mvc.perform(post("/api/usuarios/2/reportes").principal(ANA).contentType(MediaType.APPLICATION_JSON)
                .content("{\"descripcion\":\"x\"}")).andExpect(status().isBadRequest());
        mvc.perform(post("/api/usuarios/2/reportes").principal(ANA).contentType(MediaType.APPLICATION_JSON)
                .content("{\"tipoReporte\":\"INVENTADO\"}")).andExpect(status().isBadRequest());
        verify(reporteService, never()).reportar(any(), any(), any());
    }
}
