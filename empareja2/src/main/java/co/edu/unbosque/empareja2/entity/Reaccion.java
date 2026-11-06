package co.edu.unbosque.empareja2.entity;

import java.time.LocalDateTime;

/**
 * Entidad DEBIL REACCION(numero_reaccion, fecha_hora_reaccion). Su llave
 * es (autor, numero_reaccion); depende del usuario que la da (relacion Dar,
 * rombo doble, 1:N). Los "me gusta" son publicos.
 *
 * El modelo no define a quien va dirigida; se agrega la relacion
 * "destinatario". Un usuario solo puede dar un "me gusta" por perfil.
 */
public class Reaccion {

    /** El usuario que da la reaccion. */
    private Usuario autor;

    /** El numero de la reaccion dentro de las de su autor. */
    private Integer numeroReaccion;

    /** El usuario que recibe la reaccion. */
    private Usuario destinatario;

    /** La fecha y hora en que se dio la reaccion. */
    private LocalDateTime fechaHora;

    /**
     * Crea un objeto Reaccion vacio; los datos se asignan con los metodos set.
     */
    public Reaccion() {
    }

    /**
     * Crea una reaccion nueva; la fecha y hora se asignan con el momento actual.
     *
     * @param autor          el usuario que da la reaccion
     * @param destinatario   el usuario que recibe la reaccion
     * @param numeroReaccion el numero de la reaccion dentro de las de su autor
     */
    public Reaccion(Usuario autor, Usuario destinatario, Integer numeroReaccion) {
        this.autor = autor;
        this.destinatario = destinatario;
        this.numeroReaccion = numeroReaccion;
        this.fechaHora = LocalDateTime.now();
    }

    /**
     * Obtiene el usuario que da la reaccion.
     *
     * @return el usuario que da la reaccion
     */
    public Usuario getAutor() {
        return autor;
    }

    /**
     * Establece el usuario que da la reaccion.
     *
     * @param autor el usuario que da la reaccion
     */
    public void setAutor(Usuario autor) {
        this.autor = autor;
    }

    /**
     * Obtiene el numero de la reaccion dentro de las de su autor.
     *
     * @return el numero de la reaccion dentro de las de su autor
     */
    public Integer getNumeroReaccion() {
        return numeroReaccion;
    }

    /**
     * Establece el numero de la reaccion dentro de las de su autor.
     *
     * @param numeroReaccion el numero de la reaccion dentro de las de su autor
     */
    public void setNumeroReaccion(Integer numeroReaccion) {
        this.numeroReaccion = numeroReaccion;
    }

    /**
     * Obtiene el usuario que recibe la reaccion.
     *
     * @return el usuario que recibe la reaccion
     */
    public Usuario getDestinatario() {
        return destinatario;
    }

    /**
     * Establece el usuario que recibe la reaccion.
     *
     * @param destinatario el usuario que recibe la reaccion
     */
    public void setDestinatario(Usuario destinatario) {
        this.destinatario = destinatario;
    }

    /**
     * Obtiene la fecha y hora en que se dio la reaccion.
     *
     * @return la fecha y hora en que se dio la reaccion
     */
    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    /**
     * Establece la fecha y hora en que se dio la reaccion.
     *
     * @param fechaHora la fecha y hora en que se dio la reaccion
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
        return "Reaccion [idAutor=" + (autor == null ? null : autor.getId())
                + ", numeroReaccion=" + numeroReaccion
                + ", idDestinatario=" + (destinatario == null ? null : destinatario.getId())
                + ", fechaHora=" + fechaHora
                + "]";
    }
}
