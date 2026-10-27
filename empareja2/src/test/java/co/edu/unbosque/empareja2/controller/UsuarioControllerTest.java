package co.edu.unbosque.empareja2.controller;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import co.edu.unbosque.empareja2.dto.request.ActualizarPerfilRequestDTO;
import co.edu.unbosque.empareja2.dto.request.EstudioRequestDTO;
import co.edu.unbosque.empareja2.dto.response.UsuarioPublicoDTO;
import co.edu.unbosque.empareja2.entity.Genero;
import co.edu.unbosque.empareja2.exception.ResourceNotFoundException;
import co.edu.unbosque.empareja2.service.UsuarioService;

@ExtendWith(MockitoExtension.class)
class UsuarioControllerTest extends ControllerTestSupport {

    @Mock
    private UsuarioService usuarioService;

    private MockMvc mvc;

    @BeforeEach
    void preparar() {
        mvc = montar(new UsuarioController(usuarioService));
    }

    private UsuarioPublicoDTO publico(long id, String nombre) {
        return new UsuarioPublicoDTO(id, nombre, "Perez", nombre + " Perez", 22, Genero.MASCULINO, null,
                List.of("cine"), List.of(), List.of(), 2L, false);
    }

    @Test
    void catalogoSinFiltroLlamaConUniversidadNula() throws Exception {
        when(usuarioService.listarCatalogo("ana@mail.com", null)).thenReturn(List.of(publico(2, "Luis")));

        mvc.perform(get("/api/usuarios").principal(ANA)).andExpect(status().isOk())
                .andExpect(jsonPath("0.id").value(2)).andExpect(jsonPath("0.nombreCompleto").value("Luis Perez"))
                .andExpect(jsonPath("0.totalMeGusta").value(2));
    }

    @Test
    void catalogoFiltraPorUniversidad() throws Exception {
        when(usuarioService.listarCatalogo("ana@mail.com", 10L)).thenReturn(List.of(publico(2, "Luis")));

        mvc.perform(get("/api/usuarios").param("universidadId", "10").principal(ANA)).andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
        verify(usuarioService).listarCatalogo("ana@mail.com", 10L);
    }

    @Test
    void laRespuestaPublicaNuncaTraeDatosSensibles() throws Exception {
        when(usuarioService.listarCatalogo("ana@mail.com", null)).thenReturn(List.of(publico(2, "Luis")));

        mvc.perform(get("/api/usuarios").principal(ANA)).andExpect(jsonPath("0.correo").doesNotExist())
                .andExpect(jsonPath("0.documento").doesNotExist())
                .andExpect(jsonPath("0.fechaNacimiento").doesNotExist())
                .andExpect(jsonPath("0.contrasenaHash").doesNotExist());
    }

    @Test
    void universidadIdNoNumericoDa400() throws Exception {
        mvc.perform(get("/api/usuarios").param("universidadId", "abc").principal(ANA))
                .andExpect(status().isBadRequest());
    }

    @Test
    void perfilInexistenteDa404ConMensaje() throws Exception {
        when(usuarioService.obtenerPerfilPublico("ana@mail.com", 99L))
                .thenThrow(new ResourceNotFoundException("No existe un usuario con id 99"));

        mvc.perform(get("/api/usuarios/99").principal(ANA)).andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("No existe un usuario con id 99"));
    }

    @Test
    void idNoNumericoDa400() throws Exception {
        mvc.perform(get("/api/usuarios/xyz").principal(ANA)).andExpect(status().isBadRequest());
    }

    @Test
    void actualizarPerfilConDatosInvalidosDa400ConDetalle() throws Exception {
        ActualizarPerfilRequestDTO invalido = new ActualizarPerfilRequestDTO(" ", "Gomez", null,
                LocalDate.now().plusDays(1), null, Set.of(), List.of(), null);

        mvc.perform(put("/api/usuarios/me").principal(ANA).contentType(MediaType.APPLICATION_JSON)
                .content(json.writeValueAsString(invalido))).andExpect(status().isBadRequest())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("nombre")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("fechaNacimiento")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("estudios")));
    }

    @Test
    void actualizarPerfilValidoLlamaAlServicio() throws Exception {
        ActualizarPerfilRequestDTO valido = new ActualizarPerfilRequestDTO("Ana", "Gomez", Genero.FEMENINO,
                LocalDate.now().minusYears(20), null, Set.of("cine"), List.of(new EstudioRequestDTO(1L, 10L, 3)),
                List.of());

        mvc.perform(put("/api/usuarios/me").principal(ANA).contentType(MediaType.APPLICATION_JSON)
                .content(json.writeValueAsString(valido))).andExpect(status().isOk());
        verify(usuarioService).actualizarPerfilPropio(org.mockito.ArgumentMatchers.eq("ana@mail.com"),
                org.mockito.ArgumentMatchers.any(ActualizarPerfilRequestDTO.class));
    }

    @Test
    void eliminarCuentaPropiaDa204() throws Exception {
        mvc.perform(delete("/api/usuarios/me").principal(ANA)).andExpect(status().isNoContent());
        verify(usuarioService).eliminarCuentaPropia("ana@mail.com");
    }
}
