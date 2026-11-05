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

import co.edu.unbosque.empareja2.dto.MeGustaDTO;
import co.edu.unbosque.empareja2.dto.ReaccionDTO;
import co.edu.unbosque.empareja2.service.ReaccionService;

/**
 * "Me gusta" a un perfil: dar, quitar y consultar. Son publicos.
 */
@RestController
@RequestMapping("/api/usuarios/{id}/me-gusta")
public class ReaccionController {

    /** Servicio de me gusta. */
    private final ReaccionService reaccionService;

    /**
     * Crea el controlador.
     *
     * @param reaccionService el servicio de me gusta
     */
    public ReaccionController(ReaccionService reaccionService) {
        this.reaccionService = reaccionService;
    }

    /**
     * Da me gusta al perfil indicado.
     *
     * @param authentication el usuario autenticado
     * @param id             el identificador del perfil
     * @return el estado de los me gusta del perfil (201)
     */
    @PostMapping
    public ResponseEntity<MeGustaDTO> darMeGusta(Authentication authentication, @PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reaccionService.darMeGusta(authentication.getName(), id));
    }

    /**
     * Quita el me gusta dado al perfil indicado.
     *
     * @param authentication el usuario autenticado
     * @param id             el identificador del perfil
     * @return el estado de los me gusta del perfil
     */
    @DeleteMapping
    public ResponseEntity<MeGustaDTO> quitarMeGusta(Authentication authentication, @PathVariable Long id) {
        return ResponseEntity.ok(reaccionService.quitarMeGusta(authentication.getName(), id));
    }

    /**
     * Lista publicamente quienes dieron me gusta al perfil indicado.
     *
     * @param id el identificador del perfil
     * @return las reacciones recibidas
     */
    @GetMapping
    public ResponseEntity<List<ReaccionDTO>> listarMeGusta(@PathVariable Long id) {
        return ResponseEntity.ok(reaccionService.listarMeGusta(id));
    }
}
