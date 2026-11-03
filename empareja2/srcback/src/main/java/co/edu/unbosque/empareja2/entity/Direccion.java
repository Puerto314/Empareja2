package co.edu.unbosque.empareja2.entity;

/**
 * Atributo compuesto "direccion" de UNIVERSIDAD: calle + barrio.
 */
public class Direccion {

    private String calle;
    private String barrio;

    public Direccion() {
    }

    public Direccion(String calle, String barrio) {
        this.calle = calle;
        this.barrio = barrio;
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
}
