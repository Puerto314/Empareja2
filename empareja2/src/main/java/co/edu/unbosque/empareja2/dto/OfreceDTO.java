package co.edu.unbosque.empareja2.dto;

/**
 * Una oferta academica: que carrera ofrece una universidad y en que facultad.
 */
public class OfreceDTO {

    /** El identificador de la carrera. */
    private Long idCarrera;

    /** El nombre de la carrera. */
    private String carrera;

    /** El identificador de la universidad. */
    private Long idUniversidad;

    /** El nombre de la universidad. */
    private String universidad;

    /** La facultad de la carrera. */
    private String facultad;

    /**
     * Crea un objeto OfreceDTO vacio; los datos se asignan con los metodos set.
     */
    public OfreceDTO() {
    }

    /**
     * Crea un objeto OfreceDTO con todos sus datos.
     *
     * @param idCarrera el identificador de la carrera
     * @param carrera el nombre de la carrera
     * @param idUniversidad el identificador de la universidad
     * @param universidad el nombre de la universidad
     * @param facultad la facultad de la carrera
     */
    public OfreceDTO(Long idCarrera, String carrera, Long idUniversidad, String universidad, String facultad) {
        this.idCarrera = idCarrera;
        this.carrera = carrera;
        this.idUniversidad = idUniversidad;
        this.universidad = universidad;
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
    public String getCarrera() {
        return carrera;
    }

    /**
     * Establece el nombre de la carrera.
     *
     * @param carrera el nombre de la carrera
     */
    public void setCarrera(String carrera) {
        this.carrera = carrera;
    }

    /**
     * Obtiene el identificador de la universidad.
     *
     * @return el identificador de la universidad
     */
    public Long getIdUniversidad() {
        return idUniversidad;
    }

    /**
     * Establece el identificador de la universidad.
     *
     * @param idUniversidad el identificador de la universidad
     */
    public void setIdUniversidad(Long idUniversidad) {
        this.idUniversidad = idUniversidad;
    }

    /**
     * Obtiene el nombre de la universidad.
     *
     * @return el nombre de la universidad
     */
    public String getUniversidad() {
        return universidad;
    }

    /**
     * Establece el nombre de la universidad.
     *
     * @param universidad el nombre de la universidad
     */
    public void setUniversidad(String universidad) {
        this.universidad = universidad;
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
        return "OfreceDTO [idCarrera=" + idCarrera
                + ", carrera=" + carrera
                + ", idUniversidad=" + idUniversidad
                + ", universidad=" + universidad
                + ", facultad=" + facultad
                + "]";
    }
}
