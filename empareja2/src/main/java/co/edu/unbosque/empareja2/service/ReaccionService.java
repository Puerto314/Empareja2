package co.edu.unbosque.empareja2.service;

import java.util.List;

import co.edu.unbosque.empareja2.dto.response.MeGustaDTO;
import co.edu.unbosque.empareja2.dto.response.ReaccionAdminDTO;
import co.edu.unbosque.empareja2.dto.response.ReaccionDTO;

/**
 * Contrato de los "me gusta" (REACCION). Los me gusta son publicos.
 */
public interface ReaccionService {

    MeGustaDTO darMeGusta(String correoActual, Long idPerfil);

    MeGustaDTO quitarMeGusta(String correoActual, Long idPerfil);

    /** Lista publica de quienes dieron me gusta a un perfil. */
    List<ReaccionDTO> listarMeGusta(Long idPerfil);

    /** Moderacion (solo ADMIN): todas las reacciones del sistema. */
    List<ReaccionAdminDTO> listarTodasParaAdmin();

    /** Moderacion (solo ADMIN): elimina una reaccion por su llave (autor + numero). */
    void eliminarPorAdmin(Long idAutor, Integer numeroReaccion);
}
