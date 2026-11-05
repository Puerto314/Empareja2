package co.edu.unbosque.empareja2.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Datos para crear o editar una universidad. La direccion es un atributo compuesto: calle + barrio.
 */
public class UniversidadRequestDTO {

    /** El nombre de la universidad. */
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 150, message = "El nombre es demasiado largo")
    private String nombre;

    /** La sigla de la universidad. */
    @NotBlank(message = "La sigla es obligatoria")
    @Size(max = 20, message = "La sigla es demasiado larga")
    private String sigla;

    /** La calle de la direccion. */
    @NotBlank(message = "La calle es obligatoria")
    @Size(max = 100, message = "La calle es demasiado larga")
    private String calle;

    /** El barrio de la direccion. */
    @NotBlank(message = "El barrio es obligatorio")
    @Size(max = 100, message = "El barrio es demasiado largo")
    private String barrio;

    /** El identificador de la ciudad donde esta la universidad. */
    @NotNull(message = "La ciudad es obligatoria")
    private Long idCiudad;

    /**
     * Crea un objeto UniversidadRequestDTO vacio; los datos se asignan con los metodos set.
     */
    public UniversidadRequestDTO() {
    }

    /**
     * Crea un objeto UniversidadRequestDTO con todos sus datos.
     *
     * @param nombre el nombre de la universidad
     * @param sigla la sigla de la universidad
     * @param calle la calle de la direccion
     * @param barrio el barrio de la direccion
     * @param idCiudad el identificador de la ciudad donde esta la universidad
     */
    public UniversidadRequestDTO(String nombre, String sigla, String calle, String barrio, Long idCiudad) {
        this.nombre = nombre;
        this.sigla = sigla;
        this.calle = calle;
        this.barrio = barrio;
        this.idCiudad = idCiudad;
    }

    /**
     * Obtiene el nombre de la universidad.
     *
     * @return el nombre de la universidad
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre de la universidad.
     *
     * @param nombre el nombre de la universidad
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene la sigla de la universidad.
     *
     * @return la sigla de la universidad
     */
    public String getSigla() {
        return sigla;
    }

    /**
     * Establece la sigla de la universidad.
     *
     * @param sigla la sigla de la universidad
     */
    public void setSigla(String sigla) {
        this.sigla = sigla;
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
     * Obtiene el identificador de la ciudad donde esta la universidad.
     *
     * @return el identificador de la ciudad donde esta la universidad
     */
    public Long getIdCiudad() {
        return idCiudad;
    }

    /**
     * Establece el identificador de la ciudad donde esta la universidad.
     *
     * @param idCiudad el identificador de la ciudad donde esta la universidad
     */
    public void setIdCiudad(Long idCiudad) {
        this.idCiudad = idCiudad;
    }

    /**
     * Devuelve una representacion en texto de este objeto para depuracion.
     *
     * @return el objeto como texto
     */
    @Override
    public String toString() {
        return "UniversidadRequestDTO [nombre=" + nombre
                + ", sigla=" + sigla
                + ", calle=" + calle
                + ", barrio=" + barrio
                + ", idCiudad=" + idCiudad
                + "]";
    }
}
