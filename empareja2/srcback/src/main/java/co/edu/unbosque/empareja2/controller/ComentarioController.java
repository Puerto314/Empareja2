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

import co.edu.unbosque.empareja2.dto.request.ComentarioRequestDTO;
import co.edu.unbosque.empareja2.dto.response.ComentarioDTO;
import co.edu.unbosque.empareja2.service.ComentarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

/**
 * Comentarios privados: solo los ve su destinatario.
 */
@RestController
@RequestMapping("/api/usuarios")
@Tag(name = "Comentarios", description = "Comentarios privados entre usuarios")
public class ComentarioController {

    private final ComentarioService comentarioService;

    public ComentarioController(ComentarioService comentarioService) {
        this.comentarioService = comentarioService;
    }

    @PostMapping("/{id}/comentarios")
    @Operation(summary = "Envia un comentario privado al usuario indicado")
    public ResponseEntity<ComentarioDTO> enviar(Authentication authentication, @PathVariable Long id,
            @Valid @RequestBody ComentarioRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(comentarioService.enviar(authentication.getName(), id, request));
    }

    @GetMapping("/me/comentarios/recibidos")
    @Operation(summary = "Lista los comentarios privados que me enviaron")
    public ResponseEntity<List<ComentarioDTO>> listarRecibidos(Authentication authentication) {
        return ResponseEntity.ok(comentarioService.listarRecibidos(authentication.getName()));
    }

    @GetMapping("/me/comentarios/enviados")
    @Operation(summary = "Lista los comentarios privados que yo envie")
    public ResponseEntity<List<ComentarioDTO>> listarEnviados(Authentication authentication) {
        return ResponseEntity.ok(comentarioService.listarEnviados(authentication.getName()));
    }
}
