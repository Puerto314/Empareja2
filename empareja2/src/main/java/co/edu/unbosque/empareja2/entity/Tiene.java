package co.edu.unbosque.empareja2.entity;

/**
 * Perfil de un estudiante en una red social: la red y su nombre de usuario en ella
 * (tabla estudiante_red_social del modelo, una fila por estudiante y red).
 */
public class Tiene {

    /** La red social. */
    private RedSocial redSocial;

    /** El nombre de usuario del estudiante en esa red. */
    private String nombreUsuario;

    /**
     * Crea un objeto Tiene vacio; los datos se asignan con los metodos set.
     */
    public Tiene() {
    }

    /**
     * Crea el perfil de un estudiante en una red social.
     *
     * @param redSocial la red social
     * @param nombreUsuario el nombre de usuario del estudiante en esa red
     */
    public Tiene(RedSocial redSocial, String nombreUsuario) {
        this.redSocial = redSocial;
        this.nombreUsuario = nombreUsuario;
    }

    /**
     * Obtiene la red social.
     *
     * @return la red social
     */
    public RedSocial getRedSocial() {
        return redSocial;
    }

    /**
     * Establece la red social.
     *
     * @param redSocial la red social
     */
    public void setRedSocial(RedSocial redSocial) {
        this.redSocial = redSocial;
    }

    /**
     * Obtiene el nombre de usuario del estudiante en esa red.
     *
     * @return el nombre de usuario del estudiante en esa red
     */
    public String getNombreUsuario() {
        return nombreUsuario;
    }

    /**
     * Establece el nombre de usuario del estudiante en esa red.
     *
     * @param nombreUsuario el nombre de usuario del estudiante en esa red
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
        return "Tiene [redSocial=" + redSocial
                + ", nombreUsuario=" + nombreUsuario
                + "]";
    }
}
