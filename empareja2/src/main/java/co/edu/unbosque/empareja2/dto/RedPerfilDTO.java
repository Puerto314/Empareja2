package co.edu.unbosque.empareja2.dto;

/**
 * El perfil de un usuario en una red social.
 */
public class RedPerfilDTO {

    /** El identificador de la red social. */
    private Long idRedSocial;

    /** El nombre de la red social. */
    private String redSocial;

    /** El nombre de usuario en esa red. */
    private String nombreUsuario;

    /**
     * Crea un objeto RedPerfilDTO vacio; los datos se asignan con los metodos set.
     */
    public RedPerfilDTO() {
    }

    /**
     * Crea un objeto RedPerfilDTO con todos sus datos.
     *
     * @param idRedSocial el identificador de la red social
     * @param redSocial el nombre de la red social
     * @param nombreUsuario el nombre de usuario en esa red
     */
    public RedPerfilDTO(Long idRedSocial, String redSocial, String nombreUsuario) {
        this.idRedSocial = idRedSocial;
        this.redSocial = redSocial;
        this.nombreUsuario = nombreUsuario;
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
    public String getRedSocial() {
        return redSocial;
    }

    /**
     * Establece el nombre de la red social.
     *
     * @param redSocial el nombre de la red social
     */
    public void setRedSocial(String redSocial) {
        this.redSocial = redSocial;
    }

    /**
     * Obtiene el nombre de usuario en esa red.
     *
     * @return el nombre de usuario en esa red
     */
    public String getNombreUsuario() {
        return nombreUsuario;
    }

    /**
     * Establece el nombre de usuario en esa red.
     *
     * @param nombreUsuario el nombre de usuario en esa red
     */
    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    /**
     * Devuelve una representacion en texto de este objeto para depuracion.
     *
     * @return el objeto como texto
     */
    @Override
    public String toString() {
        return "RedPerfilDTO [idRedSocial=" + idRedSocial
                + ", redSocial=" + redSocial
                + ", nombreUsuario=" + nombreUsuario
                + "]";
    }
}
