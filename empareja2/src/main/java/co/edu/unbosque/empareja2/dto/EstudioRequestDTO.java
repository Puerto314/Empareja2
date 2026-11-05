package co.edu.unbosque.empareja2.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * Relacion Estudiar: carrera + universidad + semestre.
 */
public class EstudioRequestDTO {

    /** El identificador de la carrera. */
    @NotNull(message = "La carrera es obligatoria")
    private Long idCarrera;

    /** El identificador de la universidad. */
    @NotNull(message = "La universidad es obligatoria")
    private Long idUniversidad;

    /** El semestre que cursa (entre 1 y 20). */
    @NotNull(message = "El semestre es obligatorio")
    @Min(value = 1, message = "El semestre debe ser mayor o igual a 1")
    @Max(value = 20, message = "El semestre no es valido")
    private Integer semestre;

    /**
     * Crea un objeto EstudioRequestDTO vacio; los datos se asignan con los metodos set.
     */
    public EstudioRequestDTO() {
    }

    /**
     * Crea un objeto EstudioRequestDTO con todos sus datos.
     *
     * @param idCarrera el identificador de la carrera
     * @param idUniversidad el identificador de la universidad
     * @param semestre el semestre que cursa (entre 1 y 20)
     */
    public EstudioRequestDTO(Long idCarrera, Long idUniversidad, Integer semestre) {
        this.idCarrera = idCarrera;
        this.idUniversidad = idUniversidad;
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
     * Obtiene el semestre que cursa (entre 1 y 20).
     *
     * @return el semestre que cursa (entre 1 y 20)
     */
    public Integer getSemestre() {
        return semestre;
    }

    /**
     * Establece el semestre que cursa (entre 1 y 20).
     *
     * @param semestre el semestre que cursa (entre 1 y 20)
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
        return "EstudioRequestDTO [idCarrera=" + idCarrera
                + ", idUniversidad=" + idUniversidad
                + ", semestre=" + semestre
                + "]";
    }
}
