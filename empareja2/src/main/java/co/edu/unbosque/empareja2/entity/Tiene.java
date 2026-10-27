package co.edu.unbosque.empareja2.entity;

/**
 * Relacion Tener (RED_SOCIAL N:M USUARIO) con los atributos "url_perfil" y
 * "alias". Vive dentro de la lista de redes de un Usuario.
 */
public class Tiene {

    private RedSocial redSocial;
    private String urlPerfil;
    private String alias;

    public Tiene() {
    }

    public Tiene(RedSocial redSocial, String urlPerfil, String alias) {
        this.redSocial = redSocial;
        this.urlPerfil = urlPerfil;
        this.alias = alias;
    }

    public RedSocial getRedSocial() {
        return redSocial;
    }

    public void setRedSocial(RedSocial redSocial) {
        this.redSocial = redSocial;
    }

    public String getUrlPerfil() {
        return urlPerfil;
    }

    public void setUrlPerfil(String urlPerfil) {
        this.urlPerfil = urlPerfil;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }
}
