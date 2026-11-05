package co.edu.unbosque.empareja2.dto;

/**
 * Respuesta tras un login o registro exitoso.
 */
public class AuthResponseDTO {

    /** El token JWT para el header Authorization. */
    private String token;

    /** El tipo de token (siempre Bearer). */
    private String tokenType;

    /** Los milisegundos de vigencia del token. */
    private Long expiresInMs;

    /** El perfil completo del usuario autenticado. */
    private UsuarioPerfilDTO usuario;

    /**
     * Crea un objeto AuthResponseDTO vacio; los datos se asignan con los metodos set.
     */
    public AuthResponseDTO() {
    }

    /**
     * Crea un objeto AuthResponseDTO con todos sus datos.
     *
     * @param token el token JWT para el header Authorization
     * @param tokenType el tipo de token (siempre Bearer)
     * @param expiresInMs los milisegundos de vigencia del token
     * @param usuario el perfil completo del usuario autenticado
     */
    public AuthResponseDTO(String token, String tokenType, Long expiresInMs, UsuarioPerfilDTO usuario) {
        this.token = token;
        this.tokenType = tokenType;
        this.expiresInMs = expiresInMs;
        this.usuario = usuario;
    }

    /**
     * Obtiene el token JWT para el header Authorization.
     *
     * @return el token JWT para el header Authorization
     */
    public String getToken() {
        return token;
    }

    /**
     * Establece el token JWT para el header Authorization.
     *
     * @param token el token JWT para el header Authorization
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * Obtiene el tipo de token (siempre Bearer).
     *
     * @return el tipo de token (siempre Bearer)
     */
    public String getTokenType() {
        return tokenType;
    }

    /**
     * Establece el tipo de token (siempre Bearer).
     *
     * @param tokenType el tipo de token (siempre Bearer)
     */
    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    /**
     * Obtiene los milisegundos de vigencia del token.
     *
     * @return los milisegundos de vigencia del token
     */
    public Long getExpiresInMs() {
        return expiresInMs;
    }

    /**
     * Establece los milisegundos de vigencia del token.
     *
     * @param expiresInMs los milisegundos de vigencia del token
     */
    public void setExpiresInMs(Long expiresInMs) {
        this.expiresInMs = expiresInMs;
    }

    /**
     * Obtiene el perfil completo del usuario autenticado.
     *
     * @return el perfil completo del usuario autenticado
     */
    public UsuarioPerfilDTO getUsuario() {
        return usuario;
    }

    /**
     * Establece el perfil completo del usuario autenticado.
     *
     * @param usuario el perfil completo del usuario autenticado
     */
    public void setUsuario(UsuarioPerfilDTO usuario) {
        this.usuario = usuario;
    }

    /**
     * Devuelve una representacion en texto de este objeto para depuracion.
     *
     * @return el objeto como texto
     */
    @Override
    public String toString() {
        return "AuthResponseDTO [token=" + token
                + ", tokenType=" + tokenType
                + ", expiresInMs=" + expiresInMs
                + ", usuario=" + usuario
                + "]";
    }
}
