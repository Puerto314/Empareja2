package co.edu.unbosque.empareja2.entity;

/**
 * Relacion ternaria Estudiar(USUARIO N, CARRERA M, UNIVERSIDAD 1) con el
 * atributo "semestre". Vive dentro de la lista de estudios de un Usuario: un
 * usuario estudia una carrera en UNA sola universidad.
 */
public class Estudio {

    /** La carrera que estudia el usuario. */
    private Carrera carrera;

    /** La universidad donde la estudia. */
    private Universidad universidad;

    /** El semestre que cursa. */
    private Integer semestre;

    /**
     * Crea un objeto Estudio vacio; los datos se asignan con los metodos set.
     */
    public Estudio() {
    }

    /**
     * Crea un estudio con su carrera, su universidad y su semestre.
     *
     * @param carrera la carrera que estudia el usuario
     * @param universidad la universidad donde la estudia
     * @param semestre el semestre que cursa
     */
    public Estudio(Carrera carrera, Universidad universidad, Integer semestre) {
        this.carrera = carrera;
        this.universidad = universidad;
        this.semestre = semestre;
    }

    /**
     * Obtiene la carrera que estudia el usuario.
     *
     * @return la carrera que estudia el usuario
     */
    public Carrera getCarrera() {
        return carrera;
    }

    /**
     * Establece la carrera que estudia el usuario.
     *
     * @param carrera la carrera que estudia el usuario
     */
    public void setCarrera(Carrera carrera) {
        this.carrera = carrera;
    }

    /**
     * Obtiene la universidad donde la estudia.
     *
     * @return la universidad donde la estudia
     */
    public Universidad getUniversidad() {
        return universidad;
    }

    /**
     * Establece la universidad donde la estudia.
     *
     * @param universidad la universidad donde la estudia
     */
    public void setUniversidad(Universidad universidad) {
        this.universidad = universidad;
    }

    /**
     * Obtiene el semestre que cursa.
     *
     * @return el semestre que cursa
     */
    public Integer getSemestre() {
        return semestre;
    }

    /**
     * Establece el semestre que cursa.
     *
     * @param semestre el semestre que cursa
     */
    public void setSemestre(Integer semestre) {
        this.semestre = semestre;
    }

    /**
     * Devuelve una representacion en texto de este objeto para depuracion.
     *
     * @return el objeto como texto
     */
    @Override
    public String toString() {
        return "Estudio [carrera=" + carrera
                + ", universidad=" + universidad
                + ", semestre=" + semestre
                + "]";
    }
}
