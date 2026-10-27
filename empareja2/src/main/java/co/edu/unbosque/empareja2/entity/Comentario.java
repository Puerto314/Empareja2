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

    private Usuario autor;
    private Integer numeroComentario;
    private Usuario destinatario;
    private String contenido;
    private LocalDateTime fechaHora;

    public Comentario() {
    }

    public Comentario(Usuario autor, Usuario destinatario, Integer numeroComentario, String contenido) {
        this.autor = autor;
        this.destinatario = destinatario;
        this.numeroComentario = numeroComentario;
        this.contenido = contenido;
        this.fechaHora = LocalDateTime.now();
    }

    public Usuario getAutor() {
        return autor;
    }

    public void setAutor(Usuario autor) {
        this.autor = autor;
    }

    public Integer getNumeroComentario() {
        return numeroComentario;
    }

    public void setNumeroComentario(Integer numeroComentario) {
        this.numeroComentario = numeroComentario;
    }

    public Usuario getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(Usuario destinatario) {
        this.destinatario = destinatario;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }
}
