package co.edu.unbosque.empareja2.service;

import java.util.List;

import co.edu.unbosque.empareja2.dto.request.ActualizarPerfilRequestDTO;
import co.edu.unbosque.empareja2.dto.request.AdminUsuarioRequestDTO;
import co.edu.unbosque.empareja2.dto.response.UsuarioPerfilDTO;
import co.edu.unbosque.empareja2.dto.response.UsuarioPublicoDTO;

/**
 * Contrato del catalogo de usuarios, del perfil propio y de la administracion
 * de usuarios (exclusiva del rol ADMIN).
 */
public interface UsuarioService {

    /**
     * Lista los DEMAS usuarios registrados (sin el que consulta) con su informacion
     * publica. Si idUniversidad no es null, solo los que estudian en esa universidad.
     */
    List<UsuarioPublicoDTO> listarCatalogo(String correoActual, Long idUniversidad);

    UsuarioPublicoDTO obtenerPerfilPublico(String correoActual, Long id);

    UsuarioPerfilDTO obtenerPerfilPropio(String correo);

    UsuarioPerfilDTO actualizarPerfilPropio(String correo, ActualizarPerfilRequestDTO request);

    void eliminarCuentaPropia(String correo);

    List<UsuarioPerfilDTO> listarParaAdmin();

    UsuarioPerfilDTO obtenerParaAdmin(Long id);

    UsuarioPerfilDTO crearPorAdmin(AdminUsuarioRequestDTO request);

    UsuarioPerfilDTO actualizarPorAdmin(Long id, AdminUsuarioRequestDTO request);

    void eliminarPorAdmin(Long id);
}
