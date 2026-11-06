package co.edu.unbosque.empareja2.dto;

/**
 * El perfil de un usuario en una red social: el tipo de red y la url de su perfil.
 */
public class RedPerfilDTO {

    /** El tipo de red social. */
    private String tipoRedSocial;

    /** La url del perfil del usuario en esa red. */
    private String urlPerfil;

    /**
     * Crea un objeto RedPerfilDTO vacio; los datos se asignan con los metodos set.
     */
    public RedPerfilDTO() {
    }

    /**
     * Crea un objeto RedPerfilDTO con todos sus datos.
     *
     * @param tipoRedSocial el tipo de red social
     * @param urlPerfil     la url del perfil del usuario en esa red
     */
    public RedPerfilDTO(String tipoRedSocial, String urlPerfil) {
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
     * Obtiene la url del perfil del usuario en esa red.
     *
     * @return la url del perfil
     */
    public String getUrlPerfil() {
        return urlPerfil;
    }

    /**
     * Establece la url del perfil del usuario en esa red.
     *
     * @param urlPerfil la url del perfil
     */
    public void setUrlPerfil(String urlPerfil) {
        this.urlPerfil = urlPerfil;
    }

    @Override
    public String toString() {
        return "RedPerfilDTO [tipoRedSocial=" + tipoRedSocial
                + ", urlPerfil=" + urlPerfil
                + "]";
    }
}
