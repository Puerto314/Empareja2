package co.edu.unbosque.empareja2.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.unbosque.empareja2.dto.ComentarioDTO;
import co.edu.unbosque.empareja2.dto.ComentarioRequestDTO;
import co.edu.unbosque.empareja2.service.ComentarioService;
import jakarta.validation.Valid;

/**
 * Comentarios privados entre usuarios: solo los ve su destinatario.
 */
@RestController
@RequestMapping("/api/usuarios")
public class ComentarioController {

    /** Servicio de comentarios. */
    private final ComentarioService comentarioService;

    /**
     * Crea el controlador.
     *
     * @param comentarioService el servicio de comentarios
     */
    public ComentarioController(ComentarioService comentarioService) {
        this.comentarioService = comentarioService;
    }

    /**
     * Envia un comentario privado al usuario indicado.
     *
     * @param authentication el usuario autenticado
     * @param id             el identificador del destinatario
     * @param request        el contenido del comentario
     * @return el comentario enviado (201)
     */
    @PostMapping("/{id}/comentarios")
    public ResponseEntity<ComentarioDTO> enviar(Authentication authentication, @PathVariable Long id,
            @Valid @RequestBody ComentarioRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(comentarioService.enviar(authentication.getName(), id, request));
    }

    /**
     * Lista los comentarios privados que me enviaron.
     *
     * @param authentication el usuario autenticado
     * @return los comentarios recibidos
     */
    @GetMapping("/me/comentarios/recibidos")
    public ResponseEntity<List<ComentarioDTO>> listarRecibidos(Authentication authentication) {
        return ResponseEntity.ok(comentarioService.listarRecibidos(authentication.getName()));
    }

    /**
     * Lista los comentarios privados que yo envie.
     *
     * @param authentication el usuario autenticado
     * @return los comentarios enviados
     */
    @GetMapping("/me/comentarios/enviados")
    public ResponseEntity<List<ComentarioDTO>> listarEnviados(Authentication authentication) {
        return ResponseEntity.ok(comentarioService.listarEnviados(authentication.getName()));
    }
}
