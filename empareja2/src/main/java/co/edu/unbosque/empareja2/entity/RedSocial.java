package co.edu.unbosque.empareja2.entity;

/**
 * Perfil de un estudiante en una red social: RED_SOCIAL(id_usuario, tipo_red_social,
 * url_perfil). Su llave es (id_usuario, tipo_red_social): un estudiante tiene como
 * maximo un perfil por tipo de red. No hay un catalogo de redes; cada fila guarda el
 * enlace al perfil de un usuario.
 */
public class RedSocial {

    /** El tipo de red social (por ejemplo Instagram o LinkedIn). */
    private String tipoRedSocial;

    /** La direccion (url) del perfil del estudiante en esa red. */
    private String urlPerfil;

    /**
     * Crea un objeto RedSocial vacio; los datos se asignan con los metodos set.
     */
    public RedSocial() {
    }

    /**
     * Crea el perfil de un estudiante en una red social.
     *
     * @param tipoRedSocial el tipo de red social
     * @param urlPerfil     la url del perfil del estudiante en esa red
     */
    public RedSocial(String tipoRedSocial, String urlPerfil) {
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
        return "RedSocial [tipoRedSocial=" + tipoRedSocial
                + ", urlPerfil=" + urlPerfil
                + "]";
    }
}
