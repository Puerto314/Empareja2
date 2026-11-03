package co.edu.unbosque.empareja2.entity;

/**
 * Perfil de un estudiante en una red social: la red y su nombre de usuario en ella
 * (tabla red_social del modelo, una fila por estudiante y red).
 */
public class Tiene {

    private RedSocial redSocial;
    private String nombreUsuario;

    public Tiene() {
    }

    public Tiene(RedSocial redSocial, String nombreUsuario) {
        this.redSocial = redSocial;
        this.nombreUsuario = nombreUsuario;
    }

    public RedSocial getRedSocial() {
        return redSocial;
    }

    public void setRedSocial(RedSocial redSocial) {
        this.redSocial = redSocial;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }
}
