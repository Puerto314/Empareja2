package co.edu.unbosque.empareja2.entity;

import java.time.LocalDateTime;

/**
 * Entidad DEBIL REACCION(numero_reaccion, tipo_reaccion, fecha_hora). Su llave
 * es (autor, numero_reaccion); depende del usuario que la da (relacion Dar,
 * rombo doble, 1:N). Los "me gusta" son publicos.
 *
 * El modelo no define a quien va dirigida; se agrega la relacion
 * "destinatario". Un usuario solo puede dar un "me gusta" por perfil.
 */
public class Reaccion {

    private Usuario autor;
    private Integer numeroReaccion;
    private Usuario destinatario;
    private TipoReaccion tipoReaccion = TipoReaccion.ME_GUSTA;
    private LocalDateTime fechaHora;

    public Reaccion() {
    }

    public Reaccion(Usuario autor, Usuario destinatario, Integer numeroReaccion, TipoReaccion tipoReaccion) {
        this.autor = autor;
        this.destinatario = destinatario;
        this.numeroReaccion = numeroReaccion;
        this.tipoReaccion = tipoReaccion;
        this.fechaHora = LocalDateTime.now();
    }

    public Usuario getAutor() {
        return autor;
    }

    public void setAutor(Usuario autor) {
        this.autor = autor;
    }

    public Integer getNumeroReaccion() {
        return numeroReaccion;
    }

    public void setNumeroReaccion(Integer numeroReaccion) {
        this.numeroReaccion = numeroReaccion;
    }

    public Usuario getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(Usuario destinatario) {
        this.destinatario = destinatario;
    }

    public TipoReaccion getTipoReaccion() {
        return tipoReaccion;
    }

    public void setTipoReaccion(TipoReaccion tipoReaccion) {
        this.tipoReaccion = tipoReaccion;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }
}
