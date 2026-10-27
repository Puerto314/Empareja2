package co.edu.unbosque.empareja2.dto.response;

/**
 * Estado de los "me gusta" de un perfil despues de dar o quitar uno.
 */
public record MeGustaDTO(Long idUsuario, long totalMeGusta, boolean meGusta) {
}
