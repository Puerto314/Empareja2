package co.edu.unbosque.empareja2.entity;

/**
 * Entidad UNIVERSIDAD(id_universidad, direccion{calle, barrio}, nombre, sigla).
 * Relacion Ubicar: muchas universidades en una ciudad (N:1).
 */
public class Universidad {

    /** El identificador de la universidad. */
    private Long idUniversidad;

    /** El nombre de la universidad. */
    private String nombre;

    /** La sigla de la universidad. */
    private String sigla;

    /** La direccion de la universidad (calle y barrio). */
    private Direccion direccion = new Direccion();

    /** La ciudad donde esta la universidad. */
    private Ciudad ciudad;

    /**
     * Crea un objeto Universidad vacio; los datos se asignan con los metodos set.
     */
    public Universidad() {
    }

    /**
     * Crea una universidad con sus datos. Si la direccion es nula se usa una
     * direccion vacia.
     *
     * @param nombre    el nombre de la universidad
     * @param sigla     la sigla de la universidad
     * @param direccion la direccion de la universidad (puede ser null)
     * @param ciudad    la ciudad donde esta la universidad
     */
    public Universidad(String nombre, String sigla, Direccion direccion, Ciudad ciudad) {
        this.nombre = nombre;
        this.sigla = sigla;
        this.direccion = direccion != null ? direccion : new Direccion();
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
     * Obtiene la direccion de la universidad (calle y barrio).
     *
     * @return la direccion de la universidad (calle y barrio)
     */
    public Direccion getDireccion() {
        return direccion;
    }

    /**
     * Establece la direccion de la universidad (calle y barrio).
     *
     * @param direccion la direccion de la universidad (calle y barrio)
     */
    public void setDireccion(Direccion direccion) {
        this.direccion = direccion;
    }

    /**
     * Obtiene la ciudad donde esta la universidad.
     *
     * @return la ciudad donde esta la universidad
     */
    public Ciudad getCiudad() {
        return ciudad;
    }

    /**
     * Establece la ciudad donde esta la universidad.
     *
     * @param ciudad la ciudad donde esta la universidad
     */
    public void setCiudad(Ciudad ciudad) {
        this.ciudad = ciudad;
    }

    /**
     * Devuelve una representacion en texto de este objeto para depuracion.
     *
     * @return el objeto como texto
     */
    @Override
    public String toString() {
        return "Universidad [idUniversidad=" + idUniversidad
                + ", nombre=" + nombre
                + ", sigla=" + sigla
                + ", direccion=" + direccion
                + ", ciudad=" + ciudad
                + "]";
    }
}
