package co.edu.unbosque.empareja2.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Contenido de un comentario privado.
 */
public class ComentarioRequestDTO {

    /** El texto del comentario (maximo 1000 caracteres). */
    @NotBlank(message = "El comentario no puede estar vacio")
    @Size(max = 1000, message = "El comentario no puede superar 1000 caracteres")
    private String contenido;

    /**
     * Crea un objeto ComentarioRequestDTO vacio; los datos se asignan con los metodos set.
     */
    public ComentarioRequestDTO() {
    }

    /**
     * Crea un objeto ComentarioRequestDTO con todos sus datos.
     *
     * @param contenido el texto del comentario (maximo 1000 caracteres)
     */
    public ComentarioRequestDTO(String contenido) {
        this.contenido = contenido;
    }

    /**
     * Obtiene el texto del comentario (maximo 1000 caracteres).
     *
     * @return el texto del comentario (maximo 1000 caracteres)
     */
    public String getContenido() {
        return contenido;
    }

    /**
     * Establece el texto del comentario (maximo 1000 caracteres).
     *
     * @param contenido el texto del comentario (maximo 1000 caracteres)
     */
    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    /**
     * Devuelve una representacion en texto de este objeto para depuracion.
     *
     * @return el objeto como texto
     */
    @Override
    public String toString() {
        return "ComentarioRequestDTO [contenido=" + contenido
                + "]";
    }
}
