package co.edu.unbosque.empareja2.entity;

/**
 * UNIVERSIDAD(id_universidad, direccion{calle, barrio, numero_direccion},
 * nombre, sigla). Relacion Ubicar: muchas universidades en una ciudad (N:1).
 */
public class Universidad {

    private Long idUniversidad;
    private String nombre;
    private String sigla;
    private Direccion direccion = new Direccion();
    private Ciudad ciudad;

    public Universidad() {
    }

    public Universidad(String nombre, String sigla, Direccion direccion, Ciudad ciudad) {
        this.nombre = nombre;
        this.sigla = sigla;
        this.direccion = direccion != null ? direccion : new Direccion();
        this.ciudad = ciudad;
    }

    public Long getIdUniversidad() {
        return idUniversidad;
    }

    public void setIdUniversidad(Long idUniversidad) {
        this.idUniversidad = idUniversidad;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getSigla() {
        return sigla;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }

    public Direccion getDireccion() {
        return direccion;
    }

    public void setDireccion(Direccion direccion) {
        this.direccion = direccion;
    }

    public Ciudad getCiudad() {
        return ciudad;
    }

    public void setCiudad(Ciudad ciudad) {
        this.ciudad = ciudad;
    }
}
