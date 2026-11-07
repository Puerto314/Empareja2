package co.edu.unbosque.empareja2.handler;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;

import co.edu.unbosque.empareja2.dto.ErrorRespuestaDTO;
import co.edu.unbosque.empareja2.exception.LanzadorException;

/**
 * Todas las excepciones deben salir con el mismo cuerpo JSON y un mensaje legible.
 */
class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    private final WebRequest request = new ServletWebRequest(new MockHttpServletRequest("POST", "/api/admin/usuarios"));

    private ErrorRespuestaDTO cuerpo(ResponseEntity<Object> respuesta) {
        assertThat(respuesta.getBody()).isInstanceOf(ErrorRespuestaDTO.class);
        return (ErrorRespuestaDTO) respuesta.getBody();
    }

    @Test
    void laExcepcionDeNegocioConservaSuCodigoYSuMensaje() throws Exception {
        ResponseStatusException ex = catchResponseStatus();

        ResponseEntity<Object> respuesta = handler.handleException(ex, request);

        assertThat(respuesta.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        ErrorRespuestaDTO cuerpo = cuerpo(respuesta);
        assertThat(cuerpo.getMessage()).isEqualTo("Debes indicar exactamente una carrera y universidad");
        assertThat(cuerpo.getStatus()).isEqualTo(400);
        assertThat(cuerpo.getPath()).isEqualTo("/api/admin/usuarios");
    }

    @Test
    void laValidacionDelDtoListaCadaCampoFaltante() throws Exception {
        BeanPropertyBindingResult resultado = new BeanPropertyBindingResult(new Object(), "adminUsuarioRequestDTO");
        resultado.addError(new FieldError("adminUsuarioRequestDTO", "nombre", "El nombre es obligatorio"));
        resultado.addError(new FieldError("adminUsuarioRequestDTO", "apellido", "El apellido es obligatorio"));
        Method metodo = Object.class.getMethod("toString");
        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(new MethodParameter(metodo, -1),
                resultado);

        ResponseEntity<Object> respuesta = handler.handleException(ex, request);

        assertThat(respuesta.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(cuerpo(respuesta).getDetails()).containsExactly("El nombre es obligatorio",
                "El apellido es obligatorio");
    }

    @Test
    void laIntegridadDeLaBaseDeDatosResponde409() {
        ResponseEntity<Object> respuesta = handler.manejarIntegridad(
                new DataIntegrityViolationException("Duplicate entry"), request);

        assertThat(respuesta.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(cuerpo(respuesta).getMessage()).doesNotContain("Duplicate entry");
    }

    @Test
    void elAccesoDenegadoResponde403ConMensaje() {
        ResponseEntity<Object> respuesta = handler.manejarAccesoDenegado(new AccessDeniedException("x"), request);

        assertThat(respuesta.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(cuerpo(respuesta).getMessage()).contains("permiso");
    }

    @Test
    void loInesperadoResponde500SinFiltrarDetallesInternos() {
        ResponseEntity<Object> respuesta = handler.manejarInesperada(new IllegalStateException("clave secreta"),
                request);

        assertThat(respuesta.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(cuerpo(respuesta).getMessage()).doesNotContain("clave secreta");
    }

    private ResponseStatusException catchResponseStatus() {
        try {
            LanzadorException.debeIndicarUnaCarrera();
        } catch (ResponseStatusException ex) {
            return ex;
        }
        throw new AssertionError("Debia lanzar una excepcion");
    }
}
