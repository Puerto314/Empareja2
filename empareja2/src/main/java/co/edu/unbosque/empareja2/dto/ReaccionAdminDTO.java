package co.edu.unbosque.empareja2.dto;

import java.time.LocalDateTime;

import co.edu.unbosque.empareja2.entity.TipoReaccion;

/**
 * Una reaccion vista por un administrador: incluye su llave (idAutor +
 * numeroReaccion) y a quien va dirigida, para poder moderarla.
 */
public class ReaccionAdminDTO {

    /** El numero de la reaccion dentro de las de su autor. */
    private Integer numeroReaccion;

    /** El identificador del autor. */
    private Long idAutor;

    /** El nombre completo del autor. */
    private String nombreAutor;

    /** El identificador del destinatario. */
    private Long idDestinatario;

    /** El nombre completo del destinatario. */
    private String nombreDestinatario;

    /** El tipo de reaccion. */
    private TipoReaccion tipoReaccion;

    /** La fecha y hora de la reaccion. */
    private LocalDateTime fechaHora;

    /**
     * Crea un objeto ReaccionAdminDTO vacio; los datos se asignan con los metodos set.
     */
    public ReaccionAdminDTO() {
    }

    /**
     * Crea un objeto ReaccionAdminDTO con todos sus datos.
     *
     * @param numeroReaccion el numero de la reaccion dentro de las de su autor
     * @param idAutor el identificador del autor
     * @param nombreAutor el nombre completo del autor
     * @param idDestinatario el identificador del destinatario
     * @param nombreDestinatario el nombre completo del destinatario
     * @param tipoReaccion el tipo de reaccion
     * @param fechaHora la fecha y hora de la reaccion
     */
    public ReaccionAdminDTO(Integer numeroReaccion, Long idAutor, String nombreAutor, Long idDestinatario,
            String nombreDestinatario, TipoReaccion tipoReaccion, LocalDateTime fechaHora) {
        this.numeroReaccion = numeroReaccion;
        this.idAutor = idAutor;
        this.nombreAutor = nombreAutor;
        this.idDestinatario = idDestinatario;
        this.nombreDestinatario = nombreDestinatario;
        this.tipoReaccion = tipoReaccion;
        this.fechaHora = fechaHora;
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
     * Obtiene el tipo de reaccion.
     *
     * @return el tipo de reaccion
     */
    public TipoReaccion getTipoReaccion() {
        return tipoReaccion;
    }

    /**
     * Establece el tipo de reaccion.
     *
     * @param tipoReaccion el tipo de reaccion
     */
    public void setTipoReaccion(TipoReaccion tipoReaccion) {
        this.tipoReaccion = tipoReaccion;
    }

    /**
     * Obtiene la fecha y hora de la reaccion.
     *
     * @return la fecha y hora de la reaccion
     */
    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    /**
     * Establece la fecha y hora de la reaccion.
     *
     * @param fechaHora la fecha y hora de la reaccion
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
        return "ReaccionAdminDTO [numeroReaccion=" + numeroReaccion
                + ", idAutor=" + idAutor
                + ", nombreAutor=" + nombreAutor
                + ", idDestinatario=" + idDestinatario
                + ", nombreDestinatario=" + nombreDestinatario
                + ", tipoReaccion=" + tipoReaccion
                + ", fechaHora=" + fechaHora
                + "]";
    }
}
