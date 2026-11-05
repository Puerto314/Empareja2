package co.edu.unbosque.empareja2.dto;

import java.time.LocalDateTime;

import co.edu.unbosque.empareja2.entity.TipoReaccion;

/**
 * Una reaccion publica: quien la dio y cuando.
 */
public class ReaccionDTO {

    /** El identificador del autor. */
    private Long idAutor;

    /** El nombre completo del autor. */
    private String nombreAutor;

    /** La foto de perfil del autor. */
    private String fotoAutor;

    /** El tipo de reaccion. */
    private TipoReaccion tipoReaccion;

    /** La fecha y hora de la reaccion. */
    private LocalDateTime fechaHora;

    /**
     * Crea un objeto ReaccionDTO vacio; los datos se asignan con los metodos set.
     */
    public ReaccionDTO() {
    }

    /**
     * Crea un objeto ReaccionDTO con todos sus datos.
     *
     * @param idAutor el identificador del autor
     * @param nombreAutor el nombre completo del autor
     * @param fotoAutor la foto de perfil del autor
     * @param tipoReaccion el tipo de reaccion
     * @param fechaHora la fecha y hora de la reaccion
     */
    public ReaccionDTO(Long idAutor, String nombreAutor, String fotoAutor, TipoReaccion tipoReaccion,
            LocalDateTime fechaHora) {
        this.idAutor = idAutor;
        this.nombreAutor = nombreAutor;
        this.fotoAutor = fotoAutor;
        this.tipoReaccion = tipoReaccion;
        this.fechaHora = fechaHora;
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
     * Obtiene la foto de perfil del autor.
     *
     * @return la foto de perfil del autor
     */
    public String getFotoAutor() {
        return fotoAutor;
    }

    /**
     * Establece la foto de perfil del autor.
     *
     * @param fotoAutor la foto de perfil del autor
     */
    public void setFotoAutor(String fotoAutor) {
        this.fotoAutor = fotoAutor;
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
        return "ReaccionDTO [idAutor=" + idAutor
                + ", nombreAutor=" + nombreAutor
                + ", fotoAutor=" + fotoAutor
                + ", tipoReaccion=" + tipoReaccion
                + ", fechaHora=" + fechaHora
                + "]";
    }
}
