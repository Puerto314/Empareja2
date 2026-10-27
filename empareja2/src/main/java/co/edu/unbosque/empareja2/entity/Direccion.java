package co.edu.unbosque.empareja2.entity;

/**
 * Atributo compuesto "direccion" de UNIVERSIDAD: calle + barrio + numero_direccion.
 */
public class Direccion {

    private String calle;
    private String barrio;
    private String numeroDireccion;

    public Direccion() {
    }

    public Direccion(String calle, String barrio, String numeroDireccion) {
        this.calle = calle;
        this.barrio = barrio;
        this.numeroDireccion = numeroDireccion;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public String getBarrio() {
        return barrio;
    }

    public void setBarrio(String barrio) {
        this.barrio = barrio;
    }

    public String getNumeroDireccion() {
        return numeroDireccion;
    }

    public void setNumeroDireccion(String numeroDireccion) {
        this.numeroDireccion = numeroDireccion;
    }
}
