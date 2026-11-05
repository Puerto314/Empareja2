package co.edu.unbosque.empareja2.entity;

/**
 * Atributo compuesto "direccion" de UNIVERSIDAD: calle + barrio.
 */
public class Direccion {

    /** La calle de la direccion. */
    private String calle;

    /** El barrio de la direccion. */
    private String barrio;

    /**
     * Crea un objeto Direccion vacio; los datos se asignan con los metodos set.
     */
    public Direccion() {
    }

    /**
     * Crea una direccion con su calle y su barrio.
     *
     * @param calle la calle de la direccion
     * @param barrio el barrio de la direccion
     */
    public Direccion(String calle, String barrio) {
        this.calle = calle;
        this.barrio = barrio;
    }

    /**
     * Obtiene la calle de la direccion.
     *
     * @return la calle de la direccion
     */
    public String getCalle() {
        return calle;
    }

    /**
     * Establece la calle de la direccion.
     *
     * @param calle la calle de la direccion
     */
    public void setCalle(String calle) {
        this.calle = calle;
    }

    /**
     * Obtiene el barrio de la direccion.
     *
     * @return el barrio de la direccion
     */
    public String getBarrio() {
        return barrio;
    }

    /**
     * Establece el barrio de la direccion.
     *
     * @param barrio el barrio de la direccion
     */
    public void setBarrio(String barrio) {
        this.barrio = barrio;
    }

    /**
     * Devuelve una representacion en texto de este objeto para depuracion.
     *
     * @return el objeto como texto
     */
    @Override
    public String toString() {
        return "Direccion [calle=" + calle
                + ", barrio=" + barrio
                + "]";
    }
}
