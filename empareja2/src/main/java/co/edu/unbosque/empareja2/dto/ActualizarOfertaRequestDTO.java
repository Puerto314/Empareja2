package co.edu.unbosque.empareja2.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Datos editables de una oferta academica: la carrera y la universidad forman la
 * llave y no cambian; solo se corrige la facultad.
 */
public class ActualizarOfertaRequestDTO {

    /** La nueva facultad de la carrera. */
    @NotBlank(message = "La facultad es obligatoria")
    @Size(max = 100, message = "La facultad es demasiado larga")
    private String facultad;

    /**
     * Crea un objeto ActualizarOfertaRequestDTO vacio; los datos se asignan con los metodos set.
     */
    public ActualizarOfertaRequestDTO() {
    }

    /**
     * Crea un objeto ActualizarOfertaRequestDTO con todos sus datos.
     *
     * @param facultad la nueva facultad de la carrera
     */
    public ActualizarOfertaRequestDTO(String facultad) {
        this.facultad = facultad;
    }

    /**
     * Obtiene la nueva facultad de la carrera.
     *
     * @return la nueva facultad de la carrera
     */
    public String getFacultad() {
        return facultad;
    }

    /**
     * Establece la nueva facultad de la carrera.
     *
     * @param facultad la nueva facultad de la carrera
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
        return "ActualizarOfertaRequestDTO [facultad=" + facultad
                + "]";
    }
}
