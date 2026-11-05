package co.edu.unbosque.empareja2.entity;

/**
 * Entidad CIUDAD(id_ciudad, nombre, departamento).
 */
public class Ciudad {

    /** El identificador de la ciudad. */
    private Long idCiudad;

    /** El nombre de la ciudad. */
    private String nombre;

    /** El departamento al que pertenece la ciudad. */
    private String departamento;

    /**
     * Crea un objeto Ciudad vacio; los datos se asignan con los metodos set.
     */
    public Ciudad() {
    }

    /**
     * Crea una ciudad con su nombre y su departamento.
     *
     * @param nombre       el nombre de la ciudad
     * @param departamento el departamento al que pertenece la ciudad
     */
    public Ciudad(String nombre, String departamento) {
        this.nombre = nombre;
        this.departamento = departamento;
    }

    /**
     * Obtiene el identificador de la ciudad.
     *
     * @return el identificador de la ciudad
     */
    public Long getIdCiudad() {
        return idCiudad;
    }

    /**
     * Establece el identificador de la ciudad.
     *
     * @param idCiudad el identificador de la ciudad
     */
    public void setIdCiudad(Long idCiudad) {
        this.idCiudad = idCiudad;
    }

    /**
     * Obtiene el nombre de la ciudad.
     *
     * @return el nombre de la ciudad
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre de la ciudad.
     *
     * @param nombre el nombre de la ciudad
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene el departamento al que pertenece la ciudad.
     *
     * @return el departamento al que pertenece la ciudad
     */
    public String getDepartamento() {
        return departamento;
    }

    /**
     * Establece el departamento al que pertenece la ciudad.
     *
     * @param departamento el departamento al que pertenece la ciudad
     */
    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    /**
     * Devuelve una representacion en texto de este objeto para depuracion.
     *
     * @return el objeto como texto
     */
    @Override
    public String toString() {
        return "Ciudad [idCiudad=" + idCiudad
                + ", nombre=" + nombre
                + ", departamento=" + departamento
                + "]";
    }
}
