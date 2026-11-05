package co.edu.unbosque.empareja2.dto;

/**
 * Una red social del catalogo.
 */
public class RedSocialDTO {

    /** El identificador de la red social. */
    private Long idRedSocial;

    /** El nombre de la red social. */
    private String nombreRedSocial;

    /** La direccion base de la red social. */
    private String url;

    /**
     * Crea un objeto RedSocialDTO vacio; los datos se asignan con los metodos set.
     */
    public RedSocialDTO() {
    }

    /**
     * Crea un objeto RedSocialDTO con todos sus datos.
     *
     * @param idRedSocial el identificador de la red social
     * @param nombreRedSocial el nombre de la red social
     * @param url la direccion base de la red social
     */
    public RedSocialDTO(Long idRedSocial, String nombreRedSocial, String url) {
        this.idRedSocial = idRedSocial;
        this.nombreRedSocial = nombreRedSocial;
        this.url = url;
    }

    /**
     * Obtiene el identificador de la red social.
     *
     * @return el identificador de la red social
     */
    public Long getIdRedSocial() {
        return idRedSocial;
    }

    /**
     * Establece el identificador de la red social.
     *
     * @param idRedSocial el identificador de la red social
     */
    public void setIdRedSocial(Long idRedSocial) {
        this.idRedSocial = idRedSocial;
    }

    /**
     * Obtiene el nombre de la red social.
     *
     * @return el nombre de la red social
     */
    public String getNombreRedSocial() {
        return nombreRedSocial;
    }

    /**
     * Establece el nombre de la red social.
     *
     * @param nombreRedSocial el nombre de la red social
     */
    public void setNombreRedSocial(String nombreRedSocial) {
        this.nombreRedSocial = nombreRedSocial;
    }

    /**
     * Obtiene la direccion base de la red social.
     *
     * @return la direccion base de la red social
     */
    public String getUrl() {
        return url;
    }

    /**
     * Establece la direccion base de la red social.
     *
     * @param url la direccion base de la red social
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * Devuelve una representacion en texto de este objeto para depuracion.
     *
     * @return el objeto como texto
     */
    @Override
    public String toString() {
        return "RedSocialDTO [idRedSocial=" + idRedSocial
                + ", nombreRedSocial=" + nombreRedSocial
                + ", url=" + url
                + "]";
    }
}
