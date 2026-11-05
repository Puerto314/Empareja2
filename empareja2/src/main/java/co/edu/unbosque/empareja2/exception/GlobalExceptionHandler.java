package co.edu.unbosque.empareja2.exception;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import co.edu.unbosque.empareja2.dto.ErrorResponseDTO;

/**
 * Centraliza el manejo de errores para que todos los controladores respondan
 * con una estructura consistente (ErrorResponseDTO) en vez de stack traces
 * crudos.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /** Registro de eventos, se usa para guardar los errores inesperados. */
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Responde 404 cuando un recurso no existe.
     *
     * @param ex      la excepcion lanzada
     * @param request la peticion que fallo
     * @return la respuesta de error con estado 404
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleNotFound(ResourceNotFoundException ex, WebRequest request) {
        return build(HttpStatus.NOT_FOUND, ex.getMessage(), request, null);
    }

    /**
     * Responde 409 cuando las reglas del negocio no permiten la operacion.
     *
     * @param ex      la excepcion lanzada
     * @param request la peticion que fallo
     * @return la respuesta de error con estado 409
     */
    @ExceptionHandler(OperationNotAllowedException.class)
    public ResponseEntity<ErrorResponseDTO> handleNotAllowed(OperationNotAllowedException ex, WebRequest request) {
        return build(HttpStatus.CONFLICT, ex.getMessage(), request, null);
    }

    /**
     * Responde 409 cuando se intenta guardar un dato unico que ya existe.
     *
     * @param ex      la excepcion lanzada
     * @param request la peticion que fallo
     * @return la respuesta de error con estado 409
     */
    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ErrorResponseDTO> handleDuplicate(DuplicateResourceException ex, WebRequest request) {
        return build(HttpStatus.CONFLICT, ex.getMessage(), request, null);
    }

    /**
     * Respaldo ante carreras de concurrencia sobre restricciones unicas (ej: dos
     * "me gusta" simultaneos). Responde 409.
     *
     * @param ex      la excepcion lanzada por la base de datos
     * @param request la peticion que fallo
     * @return la respuesta de error con estado 409
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponseDTO> handleIntegrity(DataIntegrityViolationException ex, WebRequest request) {
        return build(HttpStatus.CONFLICT, "La operacion entra en conflicto con datos existentes", request, null);
    }

    /**
     * Responde 401 cuando las credenciales son invalidas.
     *
     * @param ex      la excepcion de autenticacion
     * @param request la peticion que fallo
     * @return la respuesta de error con estado 401
     */
    @ExceptionHandler({ BadCredentialsException.class, AuthenticationException.class })
    public ResponseEntity<ErrorResponseDTO> handleAuth(Exception ex, WebRequest request) {
        return build(HttpStatus.UNAUTHORIZED, "Credenciales invalidas", request, null);
    }

    /**
     * Responde 400 cuando el cliente envia un dato invalido.
     *
     * @param ex      la excepcion lanzada
     * @param request la peticion que fallo
     * @return la respuesta de error con estado 400
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponseDTO> handleIllegalArgument(IllegalArgumentException ex, WebRequest request) {
        return build(HttpStatus.BAD_REQUEST, ex.getMessage(), request, null);
    }

    /**
     * Responde 400 cuando el cuerpo de la peticion no se puede leer.
     *
     * @param ex      la excepcion lanzada
     * @param request la peticion que fallo
     * @return la respuesta de error con estado 400
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponseDTO> handleUnreadable(HttpMessageNotReadableException ex,
            WebRequest request) {
        return build(HttpStatus.BAD_REQUEST, "El cuerpo de la peticion es invalido o esta mal formado", request,
                null);
    }

    /**
     * Responde 400 cuando un parametro de la ruta o de la consulta tiene un tipo
     * incorrecto.
     *
     * @param ex      la excepcion lanzada
     * @param request la peticion que fallo
     * @return la respuesta de error con estado 400
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponseDTO> handleTypeMismatch(MethodArgumentTypeMismatchException ex,
            WebRequest request) {
        return build(HttpStatus.BAD_REQUEST, "El parametro '" + ex.getName() + "' tiene un valor invalido", request,
                null);
    }

    /**
     * Responde 405 cuando se usa un metodo HTTP que la ruta no admite.
     *
     * @param ex      la excepcion lanzada
     * @param request la peticion que fallo
     * @return la respuesta de error con estado 405
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponseDTO> handleMethodNotAllowed(HttpRequestMethodNotSupportedException ex,
            WebRequest request) {
        return build(HttpStatus.METHOD_NOT_ALLOWED, ex.getMessage(), request, null);
    }

    /**
     * Responde 400 con el detalle de cada campo que no paso la validacion.
     *
     * @param ex      la excepcion lanzada por la validacion del cuerpo
     * @param request la peticion que fallo
     * @return la respuesta de error con estado 400 y la lista de campos invalidos
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidation(MethodArgumentNotValidException ex, WebRequest request) {
        List<String> details = ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage()).toList();
        return build(HttpStatus.BAD_REQUEST, "Error de validacion en los datos enviados", request, details);
    }

    /**
     * Responde 400 cuando falta un parametro obligatorio de la consulta.
     *
     * @param ex      la excepcion lanzada
     * @param request la peticion que fallo
     * @return la respuesta de error con estado 400
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponseDTO> handleMissingParam(MissingServletRequestParameterException ex,
            WebRequest request) {
        return build(HttpStatus.BAD_REQUEST, "Falta el parametro '" + ex.getParameterName() + "'", request, null);
    }

    /**
     * Responde 415 cuando el cuerpo no se envia como JSON.
     *
     * @param ex      la excepcion lanzada
     * @param request la peticion que fallo
     * @return la respuesta de error con estado 415
     */
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ErrorResponseDTO> handleMediaType(HttpMediaTypeNotSupportedException ex,
            WebRequest request) {
        return build(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "El tipo de contenido no es soportado; usa application/json",
                request, null);
    }

    /**
     * Ruta inexistente: antes caia en el manejador generico y respondia 500; ahora
     * responde 404.
     *
     * @param ex      la excepcion lanzada
     * @param request la peticion que fallo
     * @return la respuesta de error con estado 404
     */
    @ExceptionHandler({ NoResourceFoundException.class, NoHandlerFoundException.class })
    public ResponseEntity<ErrorResponseDTO> handleNoResource(Exception ex, WebRequest request) {
        return build(HttpStatus.NOT_FOUND, "El recurso solicitado no existe", request, null);
    }

    /**
     * Responde 403 cuando el usuario no tiene permiso para la accion.
     *
     * @param ex      la excepcion lanzada
     * @param request la peticion que fallo
     * @return la respuesta de error con estado 403
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponseDTO> handleAccessDenied(AccessDeniedException ex, WebRequest request) {
        return build(HttpStatus.FORBIDDEN, "No tienes permiso para realizar esta accion", request, null);
    }

    /**
     * Respaldo para cualquier error que ningun otro metodo atienda: lo registra en
     * el log y responde 500 sin mostrar detalles internos al cliente.
     *
     * @param ex      la excepcion lanzada
     * @param request la peticion que fallo
     * @return la respuesta de error con estado 500
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGeneric(Exception ex, WebRequest request) {
        log.error("Error inesperado en {}", request.getDescription(false), ex);
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "Ocurrio un error inesperado en el servidor", request, null);
    }

    /**
     * Arma la respuesta de error con la estructura estandar.
     *
     * @param status  el estado HTTP de la respuesta
     * @param message el mensaje que explica el error
     * @param request la peticion que fallo (de aqui se toma la ruta)
     * @param details el detalle de los campos invalidos (puede ser null)
     * @return la respuesta lista para enviar al cliente
     */
    private ResponseEntity<ErrorResponseDTO> build(HttpStatus status, String message, WebRequest request,
            List<String> details) {
        ErrorResponseDTO body = new ErrorResponseDTO(status.value(), status.getReasonPhrase(), message,
                request.getDescription(false).replace("uri=", ""), details);
        return ResponseEntity.status(status).body(body);
    }
}
