package co.edu.unbosque.empareja2.dto;

import jakarta.validation.constraints.NotNull;

/**
 * Relacion Ofrecer: una universidad ofrece una carrera dentro de una facultad.
 */
public class OfreceRequestDTO {

    /** El identificador de la carrera. */
    @NotNull(message = "La carrera es obligatoria")
    private Long idCarrera;

    /** El identificador de la universidad. */
    @NotNull(message = "La universidad es obligatoria")
    private Long idUniversidad;

    /**
     * Crea un objeto OfreceRequestDTO vacio; los datos se asignan con los metodos set.
     */
    public OfreceRequestDTO() {
    }

    /**
     * Crea un objeto OfreceRequestDTO con todos sus datos.
     *
     * @param idCarrera el identificador de la carrera
     * @param idUniversidad el identificador de la universidad
     */
    public OfreceRequestDTO(Long idCarrera, Long idUniversidad) {
        this.idCarrera = idCarrera;
        this.idUniversidad = idUniversidad;
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
     * Devuelve una representacion en texto de este objeto para depuracion.
     *
     * @return el objeto como texto
     */
    @Override
    public String toString() {
        return "OfreceRequestDTO [idCarrera=" + idCarrera
                + ", idUniversidad=" + idUniversidad
                + "]";
    }
}
