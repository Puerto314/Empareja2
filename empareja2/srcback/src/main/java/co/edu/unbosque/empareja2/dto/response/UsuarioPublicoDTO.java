package co.edu.unbosque.empareja2.dto.response;

import java.util.List;

import co.edu.unbosque.empareja2.entity.Genero;

/**
 * Informacion NO sensible de un usuario, visible para cualquier usuario
 * autenticado. Deliberadamente NO incluye: correo, contrasena, tipo de
 * documento, documento, fecha de nacimiento ni rol. Se muestra la edad (derivada)
 * pero no la fecha exacta de nacimiento.
 */
public record UsuarioPublicoDTO(
        Long id,
        String nombre,
        String apellido,
        String nombreCompleto,
        Integer edad,
        Genero genero,
        String fotoPerfil,
        List<String> intereses,
        List<EstudioDTO> estudios,
        List<RedPerfilDTO> redes,
        long totalMeGusta,
        boolean meGustaDelUsuarioActual) {
}
