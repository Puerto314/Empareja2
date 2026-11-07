package co.edu.unbosque.empareja2.handler;

import java.time.Instant;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import co.edu.unbosque.empareja2.dto.ErrorRespuestaDTO;
import jakarta.validation.ConstraintViolationException;

/**
 * Convierte TODAS las excepciones en el mismo cuerpo JSON (ErrorRespuestaDTO) con
 * un mensaje en espanol que el front muestra tal cual. Las excepciones de negocio
 * (ResponseStatusException del LanzadorException) conservan su codigo y mensaje;
 * las validaciones de los DTO (@Valid) agregan un detalle por cada campo
 * invalido; lo inesperado responde 500 con un mensaje generico, sin filtrar
 * detalles internos.
 */
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    /** Registro de errores inesperados. */
    private static final Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Crea el manejador de excepciones.
     */
    public GlobalExceptionHandler() {
        super();
    }

    /**
     * Validaciones de los DTO (@Valid): campos obligatorios, tamanos, formatos.
     *
     * @param ex      la excepcion de validacion
     * @param headers las cabeceras
     * @param status  el codigo HTTP
     * @param request la peticion
     * @return la respuesta 400 con un detalle por cada campo invalido
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
            HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        List<String> detalles = ex.getBindingResult().getAllErrors().stream().map(this::textoDe).distinct().toList();
        return construir(HttpStatus.BAD_REQUEST, "Hay datos faltantes o invalidos", detalles, request);
    }

    /**
     * Cuerpo JSON ilegible: formato de fecha, rol o genero que no existen, etc.
     *
     * @param ex      la excepcion
     * @param headers las cabeceras
     * @param status  el codigo HTTP
     * @param request la peticion
     * @return la respuesta 400
     */
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
            HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        String mensaje = "El cuerpo de la peticion no es valido: revisa los formatos de los datos enviados "
                + "(fecha AAAA-MM-DD, rol, genero, numeros)";
        return construir(HttpStatus.BAD_REQUEST, mensaje, List.of(), request);
    }

    /**
     * Resto de excepciones de Spring MVC y las ResponseStatusException del
     * LanzadorException: se conserva su codigo y su mensaje.
     *
     * @param ex      la excepcion
     * @param body    el cuerpo sugerido por Spring (no se usa)
     * @param headers las cabeceras
     * @param status  el codigo HTTP
     * @param request la peticion
     * @return la respuesta con el formato unico de errores
     */
    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
            HttpStatusCode status, WebRequest request) {
        return construir(status, mensajeDe(ex, status), List.of(), request);
    }

    /**
     * Parametro de la URL con un valor que no se puede convertir (por ejemplo un
     * id que no es numero).
     *
     * @param ex      la excepcion
     * @param request la peticion
     * @return la respuesta 400
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Object> manejarTipoIncorrecto(MethodArgumentTypeMismatchException ex,
            WebRequest request) {
        return construir(HttpStatus.BAD_REQUEST, "El valor del parametro '" + ex.getName() + "' no es valido",
                List.of(), request);
    }

    /**
     * Validaciones de parametros (@Validated).
     *
     * @param ex      la excepcion
     * @param request la peticion
     * @return la respuesta 400
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> manejarRestricciones(ConstraintViolationException ex, WebRequest request) {
        List<String> detalles = ex.getConstraintViolations().stream().map(v -> v.getMessage()).distinct().toList();
        return construir(HttpStatus.BAD_REQUEST, "Hay datos faltantes o invalidos", detalles, request);
    }

    /**
     * Una regla de la base de datos (llave unica, llave foranea, CHECK) impidio guardar.
     *
     * @param ex      la excepcion
     * @param request la peticion
     * @return la respuesta 409
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> manejarIntegridad(DataIntegrityViolationException ex, WebRequest request) {
        LOG.warn("Violacion de integridad: {}", ex.getMostSpecificCause().getMessage());
        return construir(HttpStatus.CONFLICT,
                "No se pudo guardar: los datos chocan con un registro existente o incumplen una regla de la base de datos",
                List.of(), request);
    }

    /**
     * Credenciales o token invalidos.
     *
     * @param ex      la excepcion
     * @param request la peticion
     * @return la respuesta 401
     */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Object> manejarAutenticacion(AuthenticationException ex, WebRequest request) {
        return construir(HttpStatus.UNAUTHORIZED, "Tu sesion no es valida o expiro. Inicia sesion de nuevo",
                List.of(), request);
    }

    /**
     * El usuario no tiene permiso para la accion.
     *
     * @param ex      la excepcion
     * @param request la peticion
     * @return la respuesta 403
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> manejarAccesoDenegado(AccessDeniedException ex, WebRequest request) {
        return construir(HttpStatus.FORBIDDEN, "No tienes permiso para realizar esta accion", List.of(), request);
    }

    /**
     * Cualquier otra excepcion no prevista.
     *
     * @param ex      la excepcion
     * @param request la peticion
     * @return la respuesta 500 con un mensaje generico
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> manejarInesperada(Exception ex, WebRequest request) {
        LOG.error("Error inesperado", ex);
        return construir(HttpStatus.INTERNAL_SERVER_ERROR,
                "Ocurrio un error inesperado en el servidor. Intenta de nuevo", List.of(), request);
    }

    /**
     * El texto de un error de validacion.
     *
     * @param error el error de validacion
     * @return su mensaje, o un texto generico si no tiene
     */
    private String textoDe(ObjectError error) {
        String mensaje = error.getDefaultMessage();
        return mensaje == null || mensaje.isBlank() ? "Hay un dato invalido" : mensaje;
    }

    /**
     * El mensaje que se le muestra al usuario para una excepcion de Spring.
     *
     * @param ex     la excepcion
     * @param status el codigo HTTP
     * @return el mensaje
     */
    private String mensajeDe(Exception ex, HttpStatusCode status) {
        if (ex instanceof ResponseStatusException rse
                && rse.getReason() != null && !rse.getReason().isBlank()) {
            return rse.getReason();
        }
        if (ex instanceof MissingServletRequestParameterException falta) {
            return "Falta el parametro '" + falta.getParameterName() + "'";
        }
        return switch (status.value()) {
            case 400 -> "La peticion no es valida";
            case 404 -> "No se encontro el recurso solicitado";
            case 405 -> "Ese metodo no esta permitido para esta ruta";
            case 406, 415 -> "El formato de la peticion no es compatible";
            case 503 -> "El servicio no esta disponible en este momento";
            default -> status.is5xxServerError() ? "Ocurrio un error en el servidor. Intenta de nuevo"
                    : "No se pudo completar la accion";
        };
    }

    /**
     * Arma la respuesta con el formato unico de errores.
     *
     * @param status   el codigo HTTP
     * @param mensaje  el mensaje para el usuario
     * @param detalles los detalles adicionales
     * @param request  la peticion
     * @return la respuesta
     */
    private ResponseEntity<Object> construir(HttpStatusCode status, String mensaje, List<String> detalles,
            WebRequest request) {
        HttpStatus http = HttpStatus.resolve(status.value());
        String texto = http != null ? http.getReasonPhrase() : "Error";
        String ruta = request instanceof ServletWebRequest web ? web.getRequest().getRequestURI()
                : request.getDescription(false);
        ErrorRespuestaDTO cuerpo = new ErrorRespuestaDTO(Instant.now().toString(), status.value(), texto, mensaje,
                ruta, detalles);
        return ResponseEntity.status(status).body(cuerpo);
    }
}
