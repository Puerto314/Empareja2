package co.edu.unbosque.empareja2.entity;

/**
 * RED_SOCIAL(id_red_social, nombre_red_social, url).
 */
public class RedSocial {

    private Long idRedSocial;
    private String nombreRedSocial;
    private String url;

    public RedSocial() {
    }

    public RedSocial(String nombreRedSocial, String url) {
        this.nombreRedSocial = nombreRedSocial;
        this.url = url;
    }

    public Long getIdRedSocial() {
        return idRedSocial;
    }

    public void setIdRedSocial(Long idRedSocial) {
        this.idRedSocial = idRedSocial;
    }

    public String getNombreRedSocial() {
        return nombreRedSocial;
    }

    public void setNombreRedSocial(String nombreRedSocial) {
        this.nombreRedSocial = nombreRedSocial;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
