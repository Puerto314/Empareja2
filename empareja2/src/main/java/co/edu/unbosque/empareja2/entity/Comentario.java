package co.edu.unbosque.empareja2.entity;

import java.time.LocalDateTime;

/**
 * Entidad DEBIL COMENTARIO(numero_comentario, contenido, fecha_hora). Su llave
 * es (autor, numero_comentario) y depende del usuario que lo escribe (relacion
 * Escribir, rombo doble, 1:N).
 *
 * El modelo no define quien RECIBE el comentario; como es privado (solo lo ve
 * su destinatario) se agrega la relacion "destinatario" hacia USUARIO.
 */
public class Comentario {

    /** El usuario que escribio el comentario. */
    private Usuario autor;

    /** El numero del comentario dentro de los comentarios de su autor. */
    private Integer numeroComentario;

    /** El usuario que recibe el comentario. */
    private Usuario destinatario;

    /** El texto del comentario. */
    private String contenido;

    /** La fecha y hora en que se escribio el comentario. */
    private LocalDateTime fechaHora;

    /**
     * Crea un objeto Comentario vacio; los datos se asignan con los metodos set.
     */
    public Comentario() {
    }

    /**
     * Crea un comentario nuevo; la fecha y hora se asignan con el momento actual.
     *
     * @param autor            el usuario que escribe el comentario
     * @param destinatario     el usuario que recibe el comentario
     * @param numeroComentario el numero del comentario dentro de los de su autor
     * @param contenido        el texto del comentario
     */
    public Comentario(Usuario autor, Usuario destinatario, Integer numeroComentario, String contenido) {
        this.autor = autor;
        this.destinatario = destinatario;
        this.numeroComentario = numeroComentario;
        this.contenido = contenido;
        this.fechaHora = LocalDateTime.now();
    }

    /**
     * Obtiene el usuario que escribio el comentario.
     *
     * @return el usuario que escribio el comentario
     */
    public Usuario getAutor() {
        return autor;
    }

    /**
     * Establece el usuario que escribio el comentario.
     *
     * @param autor el usuario que escribio el comentario
     */
    public void setAutor(Usuario autor) {
        this.autor = autor;
    }

    /**
     * Obtiene el numero del comentario dentro de los comentarios de su autor.
     *
     * @return el numero del comentario dentro de los comentarios de su autor
     */
    public Integer getNumeroComentario() {
        return numeroComentario;
    }

    /**
     * Establece el numero del comentario dentro de los comentarios de su autor.
     *
     * @param numeroComentario el numero del comentario dentro de los comentarios de su autor
     */
    public void setNumeroComentario(Integer numeroComentario) {
        this.numeroComentario = numeroComentario;
    }

    /**
     * Obtiene el usuario que recibe el comentario.
     *
     * @return el usuario que recibe el comentario
     */
    public Usuario getDestinatario() {
        return destinatario;
    }

    /**
     * Establece el usuario que recibe el comentario.
     *
     * @param destinatario el usuario que recibe el comentario
     */
    public void setDestinatario(Usuario destinatario) {
        this.destinatario = destinatario;
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
     * Obtiene la fecha y hora en que se escribio el comentario.
     *
     * @return la fecha y hora en que se escribio el comentario
     */
    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    /**
     * Establece la fecha y hora en que se escribio el comentario.
     *
     * @param fechaHora la fecha y hora en que se escribio el comentario
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
        return "Comentario [idAutor=" + (autor == null ? null : autor.getId())
                + ", numeroComentario=" + numeroComentario
                + ", idDestinatario=" + (destinatario == null ? null : destinatario.getId())
                + ", contenido=" + contenido
                + ", fechaHora=" + fechaHora
                + "]";
    }
}
