package co.edu.unbosque.empareja2.dto.response;

/**
 * Respuesta tras un login o registro exitoso.
 */
public record AuthResponseDTO(String token, String tokenType, Long expiresInMs, UsuarioPerfilDTO usuario) {

    public static AuthResponseDTO bearer(String token, Long expiresInMs, UsuarioPerfilDTO usuario) {
        return new AuthResponseDTO(token, "Bearer", expiresInMs, usuario);
    }
}
