package co.edu.unbosque.empareja2.dto;

/**
 * Una universidad con su direccion y su ciudad, tal como se envia al cliente.
 */
public class UniversidadDTO {

    /** El identificador de la universidad. */
    private Long idUniversidad;

    /** El nombre de la universidad. */
    private String nombre;

    /** La sigla de la universidad. */
    private String sigla;

    /** La calle de la direccion. */
    private String calle;

    /** El barrio de la direccion. */
    private String barrio;

    /** La ciudad donde esta la universidad. */
    private CiudadDTO ciudad;

    /**
     * Crea un objeto UniversidadDTO vacio; los datos se asignan con los metodos set.
     */
    public UniversidadDTO() {
    }

    /**
     * Crea un objeto UniversidadDTO con todos sus datos.
     *
     * @param idUniversidad el identificador de la universidad
     * @param nombre el nombre de la universidad
     * @param sigla la sigla de la universidad
     * @param calle la calle de la direccion
     * @param barrio el barrio de la direccion
     * @param ciudad la ciudad donde esta la universidad
     */
    public UniversidadDTO(Long idUniversidad, String nombre, String sigla, String calle, String barrio,
            CiudadDTO ciudad) {
        this.idUniversidad = idUniversidad;
        this.nombre = nombre;
        this.sigla = sigla;
        this.calle = calle;
        this.barrio = barrio;
        this.ciudad = ciudad;
    }

    /**
     * Obtiene el identificador de la universidad.
     *
     * @return el identificador de la universidad
     */
    public Long getIdUniversidad() {
        return idUniversidad;
    }

    /**
     * Establece el identificador de la universidad.
     *
     * @param idUniversidad el identificador de la universidad
     */
    public void setIdUniversidad(Long idUniversidad) {
        this.idUniversidad = idUniversidad;
    }

    /**
     * Obtiene el nombre de la universidad.
     *
     * @return el nombre de la universidad
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre de la universidad.
     *
     * @param nombre el nombre de la universidad
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene la sigla de la universidad.
     *
     * @return la sigla de la universidad
     */
    public String getSigla() {
        return sigla;
    }

    /**
     * Establece la sigla de la universidad.
     *
     * @param sigla la sigla de la universidad
     */
    public void setSigla(String sigla) {
        this.sigla = sigla;
    }

    /**
     * Obtiene la calle de la direccion.
     *
     * @return la calle de la direccion
     */
    public String getCalle() {
        return calle;
    }

    /**
     * Establece la calle de la direccion.
     *
     * @param calle la calle de la direccion
     */
    public void setCalle(String calle) {
        this.calle = calle;
    }

    /**
     * Obtiene el barrio de la direccion.
     *
     * @return el barrio de la direccion
     */
    public String getBarrio() {
        return barrio;
    }

    /**
     * Establece el barrio de la direccion.
     *
     * @param barrio el barrio de la direccion
     */
    public void setBarrio(String barrio) {
        this.barrio = barrio;
    }

    /**
     * Obtiene la ciudad donde esta la universidad.
     *
     * @return la ciudad donde esta la universidad
     */
    public CiudadDTO getCiudad() {
        return ciudad;
    }

    /**
     * Establece la ciudad donde esta la universidad.
     *
     * @param ciudad la ciudad donde esta la universidad
     */
    public void setCiudad(CiudadDTO ciudad) {
        this.ciudad = ciudad;
    }

    /**
     * Devuelve una representacion en texto de este objeto para depuracion.
     *
     * @return el objeto como texto
     */
    @Override
    public String toString() {
        return "UniversidadDTO [idUniversidad=" + idUniversidad
                + ", nombre=" + nombre
                + ", sigla=" + sigla
                + ", calle=" + calle
                + ", barrio=" + barrio
                + ", ciudad=" + ciudad
                + "]";
    }
}
