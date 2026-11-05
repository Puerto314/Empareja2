package co.edu.unbosque.empareja2.dto;

/**
 * Resultado agregado: cuantas reacciones recibio un usuario.
 */
public class ConteoReaccionesDTO {

    /** El identificador del usuario. */
    private Long idUsuario;

    /** El numero de reacciones recibidas. */
    private Long total;

    /**
     * Crea un objeto ConteoReaccionesDTO vacio; los datos se asignan con los metodos set.
     */
    public ConteoReaccionesDTO() {
    }

    /**
     * Crea un objeto ConteoReaccionesDTO con todos sus datos.
     *
     * @param idUsuario el identificador del usuario
     * @param total el numero de reacciones recibidas
     */
    public ConteoReaccionesDTO(Long idUsuario, Long total) {
        this.idUsuario = idUsuario;
        this.total = total;
    }

    /**
     * Obtiene el identificador del usuario.
     *
     * @return el identificador del usuario
     */
    public Long getIdUsuario() {
        return idUsuario;
    }

    /**
     * Establece el identificador del usuario.
     *
     * @param idUsuario el identificador del usuario
     */
    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    /**
     * Obtiene el numero de reacciones recibidas.
     *
     * @return el numero de reacciones recibidas
     */
    public Long getTotal() {
        return total;
    }

    /**
     * Establece el numero de reacciones recibidas.
     *
     * @param total el numero de reacciones recibidas
     */
    public void setTotal(Long total) {
        this.total = total;
    }

    /**
     * Devuelve una representacion en texto de este objeto para depuracion.
     *
     * @return el objeto como texto
     */
    @Override
    public String toString() {
        return "ConteoReaccionesDTO [idUsuario=" + idUsuario
                + ", total=" + total
                + "]";
    }
}
