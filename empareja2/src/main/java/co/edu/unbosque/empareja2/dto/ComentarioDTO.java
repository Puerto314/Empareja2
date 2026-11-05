package co.edu.unbosque.empareja2.dto;

import java.time.LocalDateTime;

/**
 * Un comentario privado con el nombre de su autor y de su destinatario.
 */
public class ComentarioDTO {

    /** El numero del comentario dentro de los de su autor. */
    private Integer numeroComentario;

    /** El identificador del autor. */
    private Long idAutor;

    /** El nombre completo del autor. */
    private String nombreAutor;

    /** El identificador del destinatario. */
    private Long idDestinatario;

    /** El nombre completo del destinatario. */
    private String nombreDestinatario;

    /** El texto del comentario. */
    private String contenido;

    /** La fecha y hora del comentario. */
    private LocalDateTime fechaHora;

    /**
     * Crea un objeto ComentarioDTO vacio; los datos se asignan con los metodos set.
     */
    public ComentarioDTO() {
    }

    /**
     * Crea un objeto ComentarioDTO con todos sus datos.
     *
     * @param numeroComentario el numero del comentario dentro de los de su autor
     * @param idAutor el identificador del autor
     * @param nombreAutor el nombre completo del autor
     * @param idDestinatario el identificador del destinatario
     * @param nombreDestinatario el nombre completo del destinatario
     * @param contenido el texto del comentario
     * @param fechaHora la fecha y hora del comentario
     */
    public ComentarioDTO(Integer numeroComentario, Long idAutor, String nombreAutor, Long idDestinatario,
            String nombreDestinatario, String contenido, LocalDateTime fechaHora) {
        this.numeroComentario = numeroComentario;
        this.idAutor = idAutor;
        this.nombreAutor = nombreAutor;
        this.idDestinatario = idDestinatario;
        this.nombreDestinatario = nombreDestinatario;
        this.contenido = contenido;
        this.fechaHora = fechaHora;
    }

    /**
     * Obtiene el numero del comentario dentro de los de su autor.
     *
     * @return el numero del comentario dentro de los de su autor
     */
    public Integer getNumeroComentario() {
        return numeroComentario;
    }

    /**
     * Establece el numero del comentario dentro de los de su autor.
     *
     * @param numeroComentario el numero del comentario dentro de los de su autor
     */
    public void setNumeroComentario(Integer numeroComentario) {
        this.numeroComentario = numeroComentario;
    }

    /**
     * Obtiene el identificador del autor.
     *
     * @return el identificador del autor
     */
    public Long getIdAutor() {
        return idAutor;
    }

    /**
     * Establece el identificador del autor.
     *
     * @param idAutor el identificador del autor
     */
    public void setIdAutor(Long idAutor) {
        this.idAutor = idAutor;
    }

    /**
     * Obtiene el nombre completo del autor.
     *
     * @return el nombre completo del autor
     */
    public String getNombreAutor() {
        return nombreAutor;
    }

    /**
     * Establece el nombre completo del autor.
     *
     * @param nombreAutor el nombre completo del autor
     */
    public void setNombreAutor(String nombreAutor) {
        this.nombreAutor = nombreAutor;
    }

    /**
     * Obtiene el identificador del destinatario.
     *
     * @return el identificador del destinatario
     */
    public Long getIdDestinatario() {
        return idDestinatario;
    }

    /**
     * Establece el identificador del destinatario.
     *
     * @param idDestinatario el identificador del destinatario
     */
    public void setIdDestinatario(Long idDestinatario) {
        this.idDestinatario = idDestinatario;
    }

    /**
     * Obtiene el nombre completo del destinatario.
     *
     * @return el nombre completo del destinatario
     */
    public String getNombreDestinatario() {
        return nombreDestinatario;
    }

    /**
     * Establece el nombre completo del destinatario.
     *
     * @param nombreDestinatario el nombre completo del destinatario
     */
    public void setNombreDestinatario(String nombreDestinatario) {
        this.nombreDestinatario = nombreDestinatario;
    }

    /**
     * Obtiene el texto del comentario.
     *
     * @return el texto del comentario
     */
    public String getContenido() {
        return contenido;
    }

    /**
     * Establece el texto del comentario.
     *
     * @param contenido el texto del comentario
     */
    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    /**
     * Obtiene la fecha y hora del comentario.
     *
     * @return la fecha y hora del comentario
     */
    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    /**
     * Establece la fecha y hora del comentario.
     *
     * @param fechaHora la fecha y hora del comentario
     */
    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    /**
     * Devuelve una representacion en texto de este objeto para depuracion.
     *
     * @return el objeto como texto
     */
    @Override
    public String toString() {
        return "ComentarioDTO [numeroComentario=" + numeroComentario
                + ", idAutor=" + idAutor
                + ", nombreAutor=" + nombreAutor
                + ", idDestinatario=" + idDestinatario
                + ", nombreDestinatario=" + nombreDestinatario
                + ", contenido=" + contenido
                + ", fechaHora=" + fechaHora
                + "]";
    }
}
