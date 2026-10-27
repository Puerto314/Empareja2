package co.edu.unbosque.empareja2.service.impl;

import org.springframework.stereotype.Component;

import co.edu.unbosque.empareja2.entity.Rol;
import co.edu.unbosque.empareja2.entity.Usuario;
import co.edu.unbosque.empareja2.exception.ResourceNotFoundException;
import co.edu.unbosque.empareja2.repository.UsuarioRepository;

/**
 * Busquedas de usuarios compartidas por los servicios, con el error 404
 * correspondiente cuando no existen.
 */
@Component
public class UsuarioFinder {

    private final UsuarioRepository usuarioRepository;

    public UsuarioFinder(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    /** El usuario autenticado (el "username" del sistema es el correo). */
    public Usuario porCorreo(String correo) {
        return usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new ResourceNotFoundException("No existe un usuario con correo " + correo));
    }

    /** Cualquier usuario por id (uso administrativo). */
    public Usuario porId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No existe un usuario con id " + id));
    }

    /**
     * Un usuario visible en el catalogo (rol ESTUDIANTE). Las cuentas de
     * administracion no son perfiles: no se pueden ver, reaccionar, comentar ni
     * reportar.
     */
    public Usuario estudianteVisible(Long id) {
        return usuarioRepository.findByIdAndRol(id, Rol.ESTUDIANTE)
                .orElseThrow(() -> new ResourceNotFoundException("No existe un usuario con id " + id));
    }
}
