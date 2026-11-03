package co.edu.unbosque.empareja2.dto.response;

import java.time.LocalDate;
import java.util.List;

import co.edu.unbosque.empareja2.entity.Genero;
import co.edu.unbosque.empareja2.entity.Rol;

/**
 * Perfil completo de un usuario, incluyendo sus datos privados (correo,
 * fecha de nacimiento, rol). Solo se devuelve al propio dueno o a un
 * administrador, nunca a terceros. Nunca incluye la contrasena ni su hash.
 */
public record UsuarioPerfilDTO(
        Long id,
        String correo,
        Rol rol,
        Genero genero,
        String nombre,
        String apellido,
        String nombreCompleto,
        Integer edad,
        LocalDate fechaNacimiento,
        String fotoPerfil,
        List<String> intereses,
        List<EstudioDTO> estudios,
        List<RedPerfilDTO> redes) {
}
