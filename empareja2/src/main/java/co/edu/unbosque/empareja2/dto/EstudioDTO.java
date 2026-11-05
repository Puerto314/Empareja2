package co.edu.unbosque.empareja2.dto;

/**
 * El estudio de un usuario: carrera, universidad y semestre.
 */
public class EstudioDTO {

    /** El identificador de la carrera. */
    private Long idCarrera;

    /** El nombre de la carrera. */
    private String carrera;

    /** El identificador de la universidad. */
    private Long idUniversidad;

    /** El nombre de la universidad. */
    private String universidad;

    /** La sigla de la universidad. */
    private String siglaUniversidad;

    /** El semestre que cursa. */
    private Integer semestre;

    /**
     * Crea un objeto EstudioDTO vacio; los datos se asignan con los metodos set.
     */
    public EstudioDTO() {
    }

    /**
     * Crea un objeto EstudioDTO con todos sus datos.
     *
     * @param idCarrera el identificador de la carrera
     * @param carrera el nombre de la carrera
     * @param idUniversidad el identificador de la universidad
     * @param universidad el nombre de la universidad
     * @param siglaUniversidad la sigla de la universidad
     * @param semestre el semestre que cursa
     */
    public EstudioDTO(Long idCarrera, String carrera, Long idUniversidad, String universidad,
            String siglaUniversidad, Integer semestre) {
        this.idCarrera = idCarrera;
        this.carrera = carrera;
        this.idUniversidad = idUniversidad;
        this.universidad = universidad;
        this.siglaUniversidad = siglaUniversidad;
        this.semestre = semestre;
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
     * Obtiene la sigla de la universidad.
     *
     * @return la sigla de la universidad
     */
    public String getSiglaUniversidad() {
        return siglaUniversidad;
    }

    /**
     * Establece la sigla de la universidad.
     *
     * @param siglaUniversidad la sigla de la universidad
     */
    public void setSiglaUniversidad(String siglaUniversidad) {
        this.siglaUniversidad = siglaUniversidad;
    }

    /**
     * Obtiene el semestre que cursa.
     *
     * @return el semestre que cursa
     */
    public Integer getSemestre() {
        return semestre;
    }

    /**
     * Establece el semestre que cursa.
     *
     * @param semestre el semestre que cursa
     */
    public void setSemestre(Integer semestre) {
        this.semestre = semestre;
    }

    /**
     * Devuelve una representacion en texto de este objeto para depuracion.
     *
     * @return el objeto como texto
     */
    @Override
    public String toString() {
        return "EstudioDTO [idCarrera=" + idCarrera
                + ", carrera=" + carrera
                + ", idUniversidad=" + idUniversidad
                + ", universidad=" + universidad
                + ", siglaUniversidad=" + siglaUniversidad
                + ", semestre=" + semestre
                + "]";
    }
}
