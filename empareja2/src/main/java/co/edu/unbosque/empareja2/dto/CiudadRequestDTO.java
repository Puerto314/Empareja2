package co.edu.unbosque.empareja2.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Datos para crear o editar una ciudad.
 */
public class CiudadRequestDTO {

    /** El nombre de la ciudad. */
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 80, message = "El nombre es demasiado largo")
    private String nombre;

    /** El departamento de la ciudad. */
    @NotBlank(message = "El departamento es obligatorio")
    @Size(max = 80, message = "El departamento es demasiado largo")
    private String departamento;

    /**
     * Crea un objeto CiudadRequestDTO vacio; los datos se asignan con los metodos set.
     */
    public CiudadRequestDTO() {
    }

    /**
     * Crea un objeto CiudadRequestDTO con todos sus datos.
     *
     * @param nombre el nombre de la ciudad
     * @param departamento el departamento de la ciudad
     */
    public CiudadRequestDTO(String nombre, String departamento) {
        this.nombre = nombre;
        this.departamento = departamento;
    }

    /**
     * Obtiene el nombre de la ciudad.
     *
     * @return el nombre de la ciudad
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre de la ciudad.
     *
     * @param nombre el nombre de la ciudad
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene el departamento de la ciudad.
     *
     * @return el departamento de la ciudad
     */
    public String getDepartamento() {
        return departamento;
    }

    /**
     * Establece el departamento de la ciudad.
     *
     * @param departamento el departamento de la ciudad
     */
    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    /**
     * Devuelve una representacion en texto de este objeto para depuracion.
     *
     * @return el objeto como texto
     */
    @Override
    public String toString() {
        return "CiudadRequestDTO [nombre=" + nombre
                + ", departamento=" + departamento
                + "]";
    }
}
