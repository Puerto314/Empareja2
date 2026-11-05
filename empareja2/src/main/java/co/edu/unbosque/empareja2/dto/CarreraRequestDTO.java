package co.edu.unbosque.empareja2.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Datos para crear o editar una carrera.
 */
public class CarreraRequestDTO {

    /** El nombre de la carrera. */
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre es demasiado largo")
    private String nombre;

    /** La facultad de la carrera. */
    @NotBlank(message = "La facultad es obligatoria")
    @Size(max = 100, message = "La facultad es demasiado larga")
    private String facultad;

    /**
     * Crea un objeto CarreraRequestDTO vacio; los datos se asignan con los metodos set.
     */
    public CarreraRequestDTO() {
    }

    /**
     * Crea un objeto CarreraRequestDTO con todos sus datos.
     *
     * @param nombre el nombre de la carrera
     * @param facultad la facultad de la carrera
     */
    public CarreraRequestDTO(String nombre, String facultad) {
        this.nombre = nombre;
        this.facultad = facultad;
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
        return "CarreraRequestDTO [nombre=" + nombre
                + ", facultad=" + facultad
                + "]";
    }
}
