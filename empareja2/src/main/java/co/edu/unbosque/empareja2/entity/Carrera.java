package co.edu.unbosque.empareja2.entity;

/**
 * Entidad CARRERA(id_carrera, nombre, facultad).
 * Una carrera pertenece a una facultad y la ofrecen varias universidades (relacion Ofrecer).
 */
public class Carrera {

    /** El identificador de la carrera. */
    private Long idCarrera;

    /** El nombre de la carrera. */
    private String nombre;

    /** La facultad a la que pertenece la carrera. */
    private String facultad;

    /**
     * Crea un objeto Carrera vacio; los datos se asignan con los metodos set.
     */
    public Carrera() {
    }

    /**
     * Crea una carrera solo con su nombre (por ejemplo, al leer un estudio).
     *
     * @param nombre el nombre de la carrera
     */
    public Carrera(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Crea una carrera con su nombre y su facultad.
     *
     * @param nombre   el nombre de la carrera
     * @param facultad la facultad a la que pertenece la carrera
     */
    public Carrera(String nombre, String facultad) {
        this.nombre = nombre;
        this.facultad = facultad;
    }

    /**
     * Obtiene el identificador de la carrera.
     *
     * @return el identificador de la carrera
     */
    public Long getIdCarrera() {
        return idCarrera;
    }

    /**
     * Establece el identificador de la carrera.
     *
     * @param idCarrera el identificador de la carrera
     */
    public void setIdCarrera(Long idCarrera) {
        this.idCarrera = idCarrera;
    }

    /**
     * Obtiene el nombre de la carrera.
     *
     * @return el nombre de la carrera
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre de la carrera.
     *
     * @param nombre el nombre de la carrera
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene la facultad a la que pertenece la carrera.
     *
     * @return la facultad a la que pertenece la carrera
     */
    public String getFacultad() {
        return facultad;
    }

    /**
     * Establece la facultad a la que pertenece la carrera.
     *
     * @param facultad la facultad a la que pertenece la carrera
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
        return "Carrera [idCarrera=" + idCarrera
                + ", nombre=" + nombre
                + ", facultad=" + facultad
                + "]";
    }
}
