package co.edu.unbosque.empareja2.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Tipo de red social + url del perfil del estudiante en ella (tabla red_social).
 */
public class RedPerfilRequestDTO {

    /** El tipo de red social (por ejemplo Instagram o LinkedIn). */
    @NotBlank(message = "El tipo de red social es obligatorio")
    @Size(max = 50, message = "El tipo de red social es demasiado largo")
    private String tipoRedSocial;

    /** La url del perfil del estudiante en esa red (debe empezar por http:// o https://). */
    @NotBlank(message = "La url del perfil en la red social es obligatoria")
    @Size(max = 255, message = "La url del perfil en la red social es demasiado larga")
    @Pattern(regexp = "(?i)^https?://\\S+$", message = "La url del perfil debe empezar por http:// o https://")
    private String urlPerfil;

    /**
     * Crea un objeto RedPerfilRequestDTO vacio; los datos se asignan con los metodos set.
     */
    public RedPerfilRequestDTO() {
    }

    /**
     * Crea un objeto RedPerfilRequestDTO con todos sus datos.
     *
     * @param tipoRedSocial el tipo de red social
     * @param urlPerfil     la url del perfil del estudiante en esa red
     */
    public RedPerfilRequestDTO(String tipoRedSocial, String urlPerfil) {
        this.tipoRedSocial = tipoRedSocial;
        this.urlPerfil = urlPerfil;
    }

    /**
     * Obtiene el tipo de red social.
     *
     * @return el tipo de red social
     */
    public String getTipoRedSocial() {
        return tipoRedSocial;
    }

    /**
     * Establece el tipo de red social.
     *
     * @param tipoRedSocial el tipo de red social
     */
    public void setTipoRedSocial(String tipoRedSocial) {
        this.tipoRedSocial = tipoRedSocial;
    }

    /**
     * Obtiene la url del perfil del estudiante en esa red.
     *
     * @return la url del perfil
     */
    public String getUrlPerfil() {
        return urlPerfil;
    }

    /**
     * Establece la url del perfil del estudiante en esa red.
     *
     * @param urlPerfil la url del perfil
     */
    public void setUrlPerfil(String urlPerfil) {
        this.urlPerfil = urlPerfil;
    }

    @Override
    public String toString() {
        return "RedPerfilRequestDTO [tipoRedSocial=" + tipoRedSocial
                + ", urlPerfil=" + urlPerfil
                + "]";
    }
}
