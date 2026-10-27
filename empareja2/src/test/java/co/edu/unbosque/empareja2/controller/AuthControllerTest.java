package co.edu.unbosque.empareja2.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.web.servlet.MockMvc;

import co.edu.unbosque.empareja2.dto.request.EstudioRequestDTO;
import co.edu.unbosque.empareja2.dto.request.LoginRequestDTO;
import co.edu.unbosque.empareja2.dto.request.RegistroRequestDTO;
import co.edu.unbosque.empareja2.dto.response.AuthResponseDTO;
import co.edu.unbosque.empareja2.entity.TipoDocumento;
import co.edu.unbosque.empareja2.exception.DuplicateResourceException;
import co.edu.unbosque.empareja2.service.AuthService;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest extends ControllerTestSupport {

    @Mock
    private AuthService authService;

    private MockMvc mvc;

    @BeforeEach
    void preparar() {
        mvc = montar(new AuthController(authService));
    }

    private RegistroRequestDTO registro(String correo, String contrasena, List<EstudioRequestDTO> estudios) {
        return new RegistroRequestDTO(correo, contrasena, TipoDocumento.CC, "123", null, "Ana", "Garcia",
                LocalDate.now().minusYears(21), null, Set.of(), estudios, List.of());
    }

    @Test
    void registroValidoDa201() throws Exception {
        when(authService.registrar(any())).thenReturn(AuthResponseDTO.bearer("tok", 1000L, null));

        mvc.perform(post("/api/auth/registro").contentType(MediaType.APPLICATION_JSON)
                .content(json.writeValueAsString(
                        registro("ana@mail.com", "secreto123", List.of(new EstudioRequestDTO(1L, 10L, 3))))))
                .andExpect(status().isCreated()).andExpect(jsonPath("$.token").value("tok"))
                .andExpect(jsonPath("$.tokenType").value("Bearer"));
    }

    @Test
    void registroInvalidoDa400SinLlamarAlServicio() throws Exception {
        mvc.perform(post("/api/auth/registro").contentType(MediaType.APPLICATION_JSON)
                .content(json.writeValueAsString(registro("no-es-correo", "corta", List.of()))))
                .andExpect(status().isBadRequest()).andExpect(jsonPath("$.details.length()").value(3));
        verify(authService, never()).registrar(any());
    }

    @Test
    void registroConCorreoRepetidoDa409() throws Exception {
        when(authService.registrar(any())).thenThrow(new DuplicateResourceException("Ya existe"));

        mvc.perform(post("/api/auth/registro").contentType(MediaType.APPLICATION_JSON)
                .content(json.writeValueAsString(
                        registro("ana@mail.com", "secreto123", List.of(new EstudioRequestDTO(1L, 10L, 3))))))
                .andExpect(status().isConflict());
    }

    @Test
    void loginValidoDa200() throws Exception {
        when(authService.iniciarSesion(any())).thenReturn(AuthResponseDTO.bearer("tok", 1000L, null));

        mvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON)
                .content(json.writeValueAsString(new LoginRequestDTO("ana@mail.com", "secreto123"))))
                .andExpect(status().isOk()).andExpect(jsonPath("$.token").value("tok"));
    }

    @Test
    void loginConCredencialesInvalidasDa401() throws Exception {
        when(authService.iniciarSesion(any())).thenThrow(new BadCredentialsException("mal"));

        mvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON)
                .content(json.writeValueAsString(new LoginRequestDTO("ana@mail.com", "x"))))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void loginSinCamposDa400() throws Exception {
        mvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void cuerpoMalFormadoDa400() throws Exception {
        mvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON).content("{no es json"))
                .andExpect(status().isBadRequest());
    }
}
