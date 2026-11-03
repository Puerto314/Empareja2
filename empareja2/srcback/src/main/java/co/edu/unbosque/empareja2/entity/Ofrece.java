package co.edu.unbosque.empareja2.entity;

/**
 * Relacion Ofrecer (CARRERA N:M UNIVERSIDAD) con el atributo "facultad".
 */
public class Ofrece {

    private Carrera carrera;
    private Universidad universidad;
    private String facultad;

    public Ofrece() {
    }

    public Ofrece(Carrera carrera, Universidad universidad, String facultad) {
        this.carrera = carrera;
        this.universidad = universidad;
        this.facultad = facultad;
    }

    public Carrera getCarrera() {
        return carrera;
    }

    public void setCarrera(Carrera carrera) {
        this.carrera = carrera;
    }

    public Universidad getUniversidad() {
        return universidad;
    }

    public void setUniversidad(Universidad universidad) {
        this.universidad = universidad;
    }

    public String getFacultad() {
        return facultad;
    }

    public void setFacultad(String facultad) {
        this.facultad = facultad;
    }
}
