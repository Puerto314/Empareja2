package co.edu.unbosque.empareja2.entity;

/**
 * CIUDAD(id_ciudad, nombre, departamento).
 */
public class Ciudad {

    private Long idCiudad;
    private String nombre;
    private String departamento;

    public Ciudad() {
    }

    public Ciudad(String nombre, String departamento) {
        this.nombre = nombre;
        this.departamento = departamento;
    }

    public Long getIdCiudad() {
        return idCiudad;
    }

    public void setIdCiudad(Long idCiudad) {
        this.idCiudad = idCiudad;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }
}
