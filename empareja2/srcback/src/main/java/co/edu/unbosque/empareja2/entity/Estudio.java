package co.edu.unbosque.empareja2.entity;

/**
 * Relacion ternaria Estudiar(USUARIO N, CARRERA M, UNIVERSIDAD 1) con el
 * atributo "semestre". Vive dentro de la lista de estudios de un Usuario: un
 * usuario estudia una carrera en UNA sola universidad.
 */
public class Estudio {

    private Carrera carrera;
    private Universidad universidad;
    private Integer semestre;

    public Estudio() {
    }

    public Estudio(Carrera carrera, Universidad universidad, Integer semestre) {
        this.carrera = carrera;
        this.universidad = universidad;
        this.semestre = semestre;
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

    public Integer getSemestre() {
        return semestre;
    }

    public void setSemestre(Integer semestre) {
        this.semestre = semestre;
    }
}
