package co.edu.unbosque.empareja2.entity;

/**
 * Entidad RED_SOCIAL(id_red_social, nombre_red_social, url).
 */
public class RedSocial {

    /** El identificador de la red social. */
    private Long idRedSocial;

    /** El nombre de la red social. */
    private String nombreRedSocial;

    /** La direccion base de la red social. */
    private String url;

    /**
     * Crea un objeto RedSocial vacio; los datos se asignan con los metodos set.
     */
    public RedSocial() {
    }

    /**
     * Crea una red social con su nombre y su direccion base.
     *
     * @param nombreRedSocial el nombre de la red social
     * @param url             la direccion base de la red social
     */
    public RedSocial(String nombreRedSocial, String url) {
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
        return "RedSocial [idRedSocial=" + idRedSocial
                + ", nombreRedSocial=" + nombreRedSocial
                + ", url=" + url
                + "]";
    }
}
