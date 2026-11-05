package co.edu.unbosque.empareja2.service;

import java.util.List;

import co.edu.unbosque.empareja2.dto.MeGustaDTO;
import co.edu.unbosque.empareja2.dto.ReaccionAdminDTO;
import co.edu.unbosque.empareja2.dto.ReaccionDTO;

/**
 * Contrato de los "me gusta" (REACCION). Los me gusta son publicos.
 */
public interface ReaccionService {

    /**
     * Da me gusta a un perfil.
     *
     * @param correoActual el correo del usuario que da el me gusta
     * @param idPerfil     el identificador del perfil
     * @return el estado de los me gusta del perfil
     */
    MeGustaDTO darMeGusta(String correoActual, Long idPerfil);

    /**
     * Quita el me gusta dado a un perfil.
     *
     * @param correoActual el correo del usuario que quita el me gusta
     * @param idPerfil     el identificador del perfil
     * @return el estado de los me gusta del perfil
     */
    MeGustaDTO quitarMeGusta(String correoActual, Long idPerfil);

    /**
     * Lista publica de quienes dieron me gusta a un perfil.
     *
     * @param idPerfil el identificador del perfil
     * @return las reacciones recibidas
     */
    List<ReaccionDTO> listarMeGusta(Long idPerfil);

    /**
     * Moderacion (solo ADMIN): todas las reacciones del sistema.
     *
     * @return todas las reacciones
     */
    List<ReaccionAdminDTO> listarTodasParaAdmin();

    /**
     * Moderacion (solo ADMIN): elimina una reaccion por su llave (autor + numero).
     *
     * @param idAutor        el identificador del autor
     * @param numeroReaccion el numero de la reaccion
     */
    void eliminarPorAdmin(Long idAutor, Integer numeroReaccion);
}
