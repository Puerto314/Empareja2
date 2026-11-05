package co.edu.unbosque.empareja2.service;

import java.util.List;

import co.edu.unbosque.empareja2.dto.ActualizarPerfilRequestDTO;
import co.edu.unbosque.empareja2.dto.AdminUsuarioRequestDTO;
import co.edu.unbosque.empareja2.dto.UsuarioPerfilDTO;
import co.edu.unbosque.empareja2.dto.UsuarioPublicoDTO;

/**
 * Contrato del catalogo de usuarios, del perfil propio y de la administracion
 * de usuarios (exclusiva del rol ADMIN).
 */
public interface UsuarioService {

    /**
     * Lista los DEMAS usuarios registrados (sin el que consulta) con su informacion
     * publica. Si idUniversidad no es null, solo los que estudian en esa universidad.
     *
     * @param correoActual  el correo del usuario que consulta
     * @param idUniversidad la universidad a filtrar (puede ser null)
     * @return los usuarios del catalogo
     */
    List<UsuarioPublicoDTO> listarCatalogo(String correoActual, Long idUniversidad);

    /**
     * Obtiene la informacion publica de un usuario.
     *
     * @param correoActual el correo del usuario que consulta
     * @param id           el identificador del usuario consultado
     * @return la informacion publica del usuario
     */
    UsuarioPublicoDTO obtenerPerfilPublico(String correoActual, Long id);

    /**
     * Ranking de los usuarios con mas "me gusta" (solo estudiantes con al menos
     * uno), de mas a menos. Si idUniversidad no es null, solo los de esa
     * universidad. El limite debe estar entre 1 y 100 (el front usa 20).
     *
     * @param correoActual  el correo del usuario que consulta
     * @param idUniversidad la universidad a filtrar (puede ser null)
     * @param limite        cuantos usuarios devolver como maximo
     * @return los usuarios del ranking
     */
    List<UsuarioPublicoDTO> rankingMeGusta(String correoActual, Long idUniversidad, int limite);

    /**
     * Obtiene el perfil completo del usuario autenticado.
     *
     * @param correo el correo del usuario
     * @return su perfil completo
     */
    UsuarioPerfilDTO obtenerPerfilPropio(String correo);

    /**
     * Actualiza el perfil del usuario autenticado.
     *
     * @param correo  el correo del usuario
     * @param request los datos nuevos
     * @return su perfil actualizado
     */
    UsuarioPerfilDTO actualizarPerfilPropio(String correo, ActualizarPerfilRequestDTO request);

    /**
     * Elimina la cuenta del usuario autenticado.
     *
     * @param correo el correo del usuario
     */
    void eliminarCuentaPropia(String correo);

    /**
     * Lista todos los usuarios con su perfil completo (solo ADMIN).
     *
     * @return los usuarios
     */
    List<UsuarioPerfilDTO> listarParaAdmin();

    /**
     * Obtiene el perfil completo de cualquier usuario (solo ADMIN).
     *
     * @param id el identificador del usuario
     * @return su perfil completo
     */
    UsuarioPerfilDTO obtenerParaAdmin(Long id);

    /**
     * Crea un usuario, estudiante o administrador (solo ADMIN).
     *
     * @param request los datos del usuario
     * @return el perfil del usuario creado
     */
    UsuarioPerfilDTO crearPorAdmin(AdminUsuarioRequestDTO request);

    /**
     * Edita cualquier usuario (solo ADMIN).
     *
     * @param id      el identificador del usuario
     * @param request los datos nuevos
     * @return el perfil actualizado
     */
    UsuarioPerfilDTO actualizarPorAdmin(Long id, AdminUsuarioRequestDTO request);

    /**
     * Elimina cualquier usuario (solo ADMIN).
     *
     * @param id el identificador del usuario
     */
    void eliminarPorAdmin(Long id);
}
