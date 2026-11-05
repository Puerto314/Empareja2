package co.edu.unbosque.empareja2.dto;

/**
 * Una ciudad, tal como se envia al cliente.
 */
public class CiudadDTO {

    /** El identificador de la ciudad. */
    private Long idCiudad;

    /** El nombre de la ciudad. */
    private String nombre;

    /** El departamento de la ciudad. */
    private String departamento;

    /**
     * Crea un objeto CiudadDTO vacio; los datos se asignan con los metodos set.
     */
    public CiudadDTO() {
    }

    /**
     * Crea un objeto CiudadDTO con todos sus datos.
     *
     * @param idCiudad el identificador de la ciudad
     * @param nombre el nombre de la ciudad
     * @param departamento el departamento de la ciudad
     */
    public CiudadDTO(Long idCiudad, String nombre, String departamento) {
        this.idCiudad = idCiudad;
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
     * Obtiene el departamento de la ciudad.
     *
     * @return el departamento de la ciudad
     */
    public String getDepartamento() {
        return departamento;
    }

    /**
     * Establece el departamento de la ciudad.
     *
     * @param departamento el departamento de la ciudad
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
        return "CiudadDTO [idCiudad=" + idCiudad
                + ", nombre=" + nombre
                + ", departamento=" + departamento
                + "]";
    }
}
