package co.edu.unbosque.empareja2.entity;

/**
 * Relacion Ofrecer (CARRERA N:M UNIVERSIDAD) con el atributo "facultad".
 */
public class Ofrece {

    /** La carrera ofrecida. */
    private Carrera carrera;

    /** La universidad que ofrece la carrera. */
    private Universidad universidad;

    /** La facultad de la carrera en esa oferta. */
    private String facultad;

    /**
     * Crea un objeto Ofrece vacio; los datos se asignan con los metodos set.
     */
    public Ofrece() {
    }

    /**
     * Crea una oferta academica con su carrera, su universidad y su facultad.
     *
     * @param carrera la carrera ofrecida
     * @param universidad la universidad que ofrece la carrera
     * @param facultad la facultad de la carrera en esa oferta
     */
    public Ofrece(Carrera carrera, Universidad universidad, String facultad) {
        this.carrera = carrera;
        this.universidad = universidad;
        this.facultad = facultad;
    }

    /**
     * Obtiene la carrera ofrecida.
     *
     * @return la carrera ofrecida
     */
    public Carrera getCarrera() {
        return carrera;
    }

    /**
     * Establece la carrera ofrecida.
     *
     * @param carrera la carrera ofrecida
     */
    public void setCarrera(Carrera carrera) {
        this.carrera = carrera;
    }

    /**
     * Obtiene la universidad que ofrece la carrera.
     *
     * @return la universidad que ofrece la carrera
     */
    public Universidad getUniversidad() {
        return universidad;
    }

    /**
     * Establece la universidad que ofrece la carrera.
     *
     * @param universidad la universidad que ofrece la carrera
     */
    public void setUniversidad(Universidad universidad) {
        this.universidad = universidad;
    }

    /**
     * Obtiene la facultad de la carrera en esa oferta.
     *
     * @return la facultad de la carrera en esa oferta
     */
    public String getFacultad() {
        return facultad;
    }

    /**
     * Establece la facultad de la carrera en esa oferta.
     *
     * @param facultad la facultad de la carrera en esa oferta
     */
    public void setFacultad(String facultad) {
        this.facultad = facultad;
    }

    /**
     * Devuelve una representacion en texto de este objeto para depuracion.
     *
     * @return el objeto como texto
     */
    @Override
    public String toString() {
        return "Ofrece [carrera=" + carrera
                + ", universidad=" + universidad
                + ", facultad=" + facultad
                + "]";
    }
}
