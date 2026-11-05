package co.edu.unbosque.empareja2.dto;

/**
 * Estado de los "me gusta" de un perfil despues de dar o quitar uno.
 */
public class MeGustaDTO {

    /** El identificador del perfil. */
    private Long idUsuario;

    /** El total de me gusta que tiene el perfil. */
    private long totalMeGusta;

    /** True si el usuario actual le dio me gusta al perfil. */
    private boolean meGusta;

    /**
     * Crea un objeto MeGustaDTO vacio; los datos se asignan con los metodos set.
     */
    public MeGustaDTO() {
    }

    /**
     * Crea un objeto MeGustaDTO con todos sus datos.
     *
     * @param idUsuario el identificador del perfil
     * @param totalMeGusta el total de me gusta que tiene el perfil
     * @param meGusta true si el usuario actual le dio me gusta al perfil
     */
    public MeGustaDTO(Long idUsuario, long totalMeGusta, boolean meGusta) {
        this.idUsuario = idUsuario;
        this.totalMeGusta = totalMeGusta;
        this.meGusta = meGusta;
    }

    /**
     * Obtiene el identificador del perfil.
     *
     * @return el identificador del perfil
     */
    public Long getIdUsuario() {
        return idUsuario;
    }

    /**
     * Establece el identificador del perfil.
     *
     * @param idUsuario el identificador del perfil
     */
    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    /**
     * Obtiene el total de me gusta que tiene el perfil.
     *
     * @return el total de me gusta que tiene el perfil
     */
    public long getTotalMeGusta() {
        return totalMeGusta;
    }

    /**
     * Establece el total de me gusta que tiene el perfil.
     *
     * @param totalMeGusta el total de me gusta que tiene el perfil
     */
    public void setTotalMeGusta(long totalMeGusta) {
        this.totalMeGusta = totalMeGusta;
    }

    /**
     * Obtiene true si el usuario actual le dio me gusta al perfil.
     *
     * @return true si el usuario actual le dio me gusta al perfil
     */
    public boolean isMeGusta() {
        return meGusta;
    }

    /**
     * Establece true si el usuario actual le dio me gusta al perfil.
     *
     * @param meGusta true si el usuario actual le dio me gusta al perfil
     */
    public void setMeGusta(boolean meGusta) {
        this.meGusta = meGusta;
    }

    /**
     * Devuelve una representacion en texto de este objeto para depuracion.
     *
     * @return el objeto como texto
     */
    @Override
    public String toString() {
        return "MeGustaDTO [idUsuario=" + idUsuario
                + ", totalMeGusta=" + totalMeGusta
                + ", meGusta=" + meGusta
                + "]";
    }
}
