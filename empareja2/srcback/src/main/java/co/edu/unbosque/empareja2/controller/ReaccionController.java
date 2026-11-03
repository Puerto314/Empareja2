package co.edu.unbosque.empareja2.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.unbosque.empareja2.dto.response.MeGustaDTO;
import co.edu.unbosque.empareja2.dto.response.ReaccionDTO;
import co.edu.unbosque.empareja2.service.ReaccionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * "Me gusta" a un perfil. Son publicos.
 */
@RestController
@RequestMapping("/api/usuarios/{id}/me-gusta")
@Tag(name = "Me gusta", description = "Dar, quitar y consultar los me gusta de un perfil (publicos)")
public class ReaccionController {

    private final ReaccionService reaccionService;

    public ReaccionController(ReaccionService reaccionService) {
        this.reaccionService = reaccionService;
    }

    @PostMapping
    @Operation(summary = "Da me gusta al perfil indicado")
    public ResponseEntity<MeGustaDTO> darMeGusta(Authentication authentication, @PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reaccionService.darMeGusta(authentication.getName(), id));
    }

    @DeleteMapping
    @Operation(summary = "Quita el me gusta dado al perfil indicado")
    public ResponseEntity<MeGustaDTO> quitarMeGusta(Authentication authentication, @PathVariable Long id) {
        return ResponseEntity.ok(reaccionService.quitarMeGusta(authentication.getName(), id));
    }

    @GetMapping
    @Operation(summary = "Lista publicamente quienes dieron me gusta al perfil indicado")
    public ResponseEntity<List<ReaccionDTO>> listarMeGusta(@PathVariable Long id) {
        return ResponseEntity.ok(reaccionService.listarMeGusta(id));
    }
}
