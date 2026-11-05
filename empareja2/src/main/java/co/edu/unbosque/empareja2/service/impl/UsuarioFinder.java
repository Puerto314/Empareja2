package co.edu.unbosque.empareja2.service.impl;

import org.springframework.stereotype.Component;

import co.edu.unbosque.empareja2.entity.Rol;
import co.edu.unbosque.empareja2.entity.Usuario;
import co.edu.unbosque.empareja2.exception.LanzadorException;
import co.edu.unbosque.empareja2.repository.UsuarioRepository;

/**
 * Busquedas de usuarios compartidas por los servicios, con el error 404
 * correspondiente cuando no existen.
 */
@Component
public class UsuarioFinder {

    /** Acceso a los datos de los usuarios. */
    private final UsuarioRepository usuarioRepository;

    /**
     * Crea el buscador.
     *
     * @param usuarioRepository el repositorio de usuarios
     */
    public UsuarioFinder(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * El usuario autenticado (el "username" del sistema es el correo).
     *
     * @param correo el correo del usuario
     * @return el usuario
     */
    public Usuario porCorreo(String correo) {
        Usuario usuario = usuarioRepository.buscarPorCorreo(correo).orElse(null);
        if (usuario == null) {
            LanzadorException.usuarioNoEncontradoPorCorreo(correo);
        }
        return usuario;
    }

    /**
     * Cualquier usuario por id (uso administrativo).
     *
     * @param id el identificador del usuario
     * @return el usuario
     */
    public Usuario porId(Long id) {
        Usuario usuario = usuarioRepository.buscarPorId(id).orElse(null);
        if (usuario == null) {
            LanzadorException.usuarioNoEncontradoPorId(id);
        }
        return usuario;
    }

    /**
     * Un usuario visible en el catalogo (rol ESTUDIANTE). Las cuentas de
     * administracion no son perfiles: no se pueden ver, reaccionar, comentar ni
     * reportar.
     *
     * @param id el identificador del usuario
     * @return el estudiante
     */
    public Usuario estudianteVisible(Long id) {
        Usuario usuario = usuarioRepository.buscarPorIdYRol(id, Rol.ESTUDIANTE).orElse(null);
        if (usuario == null) {
            LanzadorException.usuarioNoEncontradoPorId(id);
        }
        return usuario;
    }
}
