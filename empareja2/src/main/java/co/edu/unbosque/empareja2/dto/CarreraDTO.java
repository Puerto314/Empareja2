package co.edu.unbosque.empareja2.dto;

/**
 * Una carrera con su facultad, tal como se envia al cliente.
 */
public class CarreraDTO {

    /** El identificador de la carrera. */
    private Long idCarrera;

    /** El nombre de la carrera. */
    private String nombre;

    /** La facultad de la carrera. */
    private String facultad;

    /**
     * Crea un objeto CarreraDTO vacio; los datos se asignan con los metodos set.
     */
    public CarreraDTO() {
    }

    /**
     * Crea un objeto CarreraDTO con todos sus datos.
     *
     * @param idCarrera el identificador de la carrera
     * @param nombre el nombre de la carrera
     * @param facultad la facultad de la carrera
     */
    public CarreraDTO(Long idCarrera, String nombre, String facultad) {
        this.idCarrera = idCarrera;
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
     * Obtiene la facultad de la carrera.
     *
     * @return la facultad de la carrera
     */
    public String getFacultad() {
        return facultad;
    }

    /**
     * Establece la facultad de la carrera.
     *
     * @param facultad la facultad de la carrera
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
        return "CarreraDTO [idCarrera=" + idCarrera
                + ", nombre=" + nombre
                + ", facultad=" + facultad
                + "]";
    }
}
