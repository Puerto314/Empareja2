package co.edu.unbosque.empareja2.entity;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Entidad USUARIO. Atributos del modelo: correo, contrasena_hash, genero, rol (se
 * deduce de la tabla administrador), nombre_completo (compuesto: nombre +
 * apellido), foto_perfil, edad (derivado de fecha_nacimiento), intereses
 * (multivaluado) y fecha_nacimiento.
 *
 * Notas de implementacion:
 * - "nombre_completo" y "edad" NO se guardan: se calculan (getNombreCompleto y
 * getEdad).
 * - "intereses" (multivaluado) se guarda en la tabla aparte interes_estudiante.
 * - El estudio (carrera + universidad + semestre) es UNO por estudiante.
 */
public class Usuario {

    /** Edad minima para registrarse en el catalogo. */
    public static final int EDAD_MINIMA = 18;

    /** El identificador del usuario. */
    private Long id;

    /** El correo (nombre de usuario) con el que inicia sesion. */
    private String correo;

    /** El hash de la contrasena (nunca la contrasena en texto). */
    private String contrasenaHash;

    /** El genero del usuario. */
    private Genero genero;

    /** El rol del usuario (ESTUDIANTE o ADMIN). */
    private Rol rol = Rol.ESTUDIANTE;

    /** El nombre del usuario. */
    private String nombre;

    /** El apellido del usuario. */
    private String apellido;

    /** La direccion de la foto de perfil. */
    private String fotoPerfil;

    /** La fecha de nacimiento. */
    private LocalDate fechaNacimiento;

    /** Los intereses del usuario (atributo multivaluado). */
    private Set<String> intereses = new LinkedHashSet<>();

    /** Los estudios del usuario (carrera, universidad y semestre). */
    private List<Estudio> estudios = new ArrayList<>();

    /** Las redes sociales del usuario (tipo de red y url de su perfil en cada una). */
    private List<RedSocial> redes = new ArrayList<>();

    /**
     * Crea un usuario vacio con rol ESTUDIANTE; los datos se asignan con los metodos set.
     */
    public Usuario() {
    }

    /**
     * Atributo compuesto: nombre + apellido.
     *
     * @return el nombre y el apellido separados por un espacio
     */
    public String getNombreCompleto() {
        String n = nombre == null ? "" : nombre.trim();
        String a = apellido == null ? "" : apellido.trim();
        return (n + " " + a).trim();
    }

    /**
     * Atributo derivado: se calcula a partir de la fecha de nacimiento.
     *
     * @return la edad en anos cumplidos, o null si no hay fecha de nacimiento
     */
    public Integer getEdad() {
        return calcularEdad(fechaNacimiento, LocalDate.now());
    }

    /**
     * Calcula la edad en anos cumplidos entre dos fechas.
     *
     * @param fechaNacimiento la fecha de nacimiento (puede ser null)
     * @param hoy             la fecha contra la que se calcula la edad
     * @return la edad en anos cumplidos, o null si la fecha de nacimiento es null
     */
    public static Integer calcularEdad(LocalDate fechaNacimiento, LocalDate hoy) {
        if (fechaNacimiento == null) {
            return null;
        }
        return Period.between(fechaNacimiento, hoy).getYears();
    }

    /**
     * Obtiene el identificador del usuario.
     *
     * @return el identificador del usuario
     */
    public Long getId() {
        return id;
    }

    /**
     * Establece el identificador del usuario.
     *
     * @param id el identificador del usuario
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Obtiene el correo (nombre de usuario) con el que inicia sesion.
     *
     * @return el correo (nombre de usuario) con el que inicia sesion
     */
    public String getCorreo() {
        return correo;
    }

    /**
     * Establece el correo (nombre de usuario) con el que inicia sesion.
     *
     * @param correo el correo (nombre de usuario) con el que inicia sesion
     */
    public void setCorreo(String correo) {
        this.correo = correo;
    }

    /**
     * Obtiene el hash de la contrasena (nunca la contrasena en texto).
     *
     * @return el hash de la contrasena (nunca la contrasena en texto)
     */
    public String getContrasenaHash() {
        return contrasenaHash;
    }

    /**
     * Establece el hash de la contrasena (nunca la contrasena en texto).
     *
     * @param contrasenaHash el hash de la contrasena (nunca la contrasena en texto)
     */
    public void setContrasenaHash(String contrasenaHash) {
        this.contrasenaHash = contrasenaHash;
    }

    /**
     * Obtiene el genero del usuario.
     *
     * @return el genero del usuario
     */
    public Genero getGenero() {
        return genero;
    }

    /**
     * Establece el genero del usuario.
     *
     * @param genero el genero del usuario
     */
    public void setGenero(Genero genero) {
        this.genero = genero;
    }

    /**
     * Obtiene el rol del usuario (ESTUDIANTE o ADMIN).
     *
     * @return el rol del usuario (ESTUDIANTE o ADMIN)
     */
    public Rol getRol() {
        return rol;
    }

    /**
     * Establece el rol del usuario (ESTUDIANTE o ADMIN).
     *
     * @param rol el rol del usuario (ESTUDIANTE o ADMIN)
     */
    public void setRol(Rol rol) {
        this.rol = rol;
    }

    /**
     * Obtiene el nombre del usuario.
     *
     * @return el nombre del usuario
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre del usuario.
     *
     * @param nombre el nombre del usuario
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene el apellido del usuario.
     *
     * @return el apellido del usuario
     */
    public String getApellido() {
        return apellido;
    }

    /**
     * Establece el apellido del usuario.
     *
     * @param apellido el apellido del usuario
     */
    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    /**
     * Obtiene la direccion de la foto de perfil.
     *
     * @return la direccion de la foto de perfil
     */
    public String getFotoPerfil() {
        return fotoPerfil;
    }

    /**
     * Establece la direccion de la foto de perfil. Una foto vacia o en blanco se
     * guarda como null (NULL en la columna foto_perfil de la tabla usuario).
     *
     * @param fotoPerfil la direccion de la foto de perfil
     */
    public void setFotoPerfil(String fotoPerfil) {
        this.fotoPerfil = (fotoPerfil == null || fotoPerfil.isBlank()) ? null : fotoPerfil.trim();
    }

    /**
     * Obtiene la fecha de nacimiento.
     *
     * @return la fecha de nacimiento
     */
    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    /**
     * Establece la fecha de nacimiento.
     *
     * @param fechaNacimiento la fecha de nacimiento
     */
    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    /**
     * Obtiene los intereses del usuario (atributo multivaluado).
     *
     * @return los intereses del usuario (atributo multivaluado)
     */
    public Set<String> getIntereses() {
        return intereses;
    }

    /**
     * Establece los intereses del usuario (atributo multivaluado).
     *
     * @param intereses los intereses del usuario (atributo multivaluado)
     */
    public void setIntereses(Set<String> intereses) {
        this.intereses = intereses;
    }

    /**
     * Obtiene los estudios del usuario (carrera, universidad y semestre).
     *
     * @return los estudios del usuario (carrera, universidad y semestre)
     */
    public List<Estudio> getEstudios() {
        return estudios;
    }

    /**
     * Establece los estudios del usuario (carrera, universidad y semestre).
     *
     * @param estudios los estudios del usuario (carrera, universidad y semestre)
     */
    public void setEstudios(List<Estudio> estudios) {
        this.estudios = estudios;
    }

    /**
     * Obtiene las redes sociales del usuario (tipo de red y url de su perfil en cada una).
     *
     * @return las redes sociales del usuario
     */
    public List<RedSocial> getRedes() {
        return redes;
    }

    /**
     * Establece las redes sociales del usuario (tipo de red y url de su perfil en cada una).
     *
     * @param redes las redes sociales del usuario
     */
    public void setRedes(List<RedSocial> redes) {
        this.redes = redes;
    }

    /**
     * Devuelve una representacion en texto de este objeto para depuracion.
     * No incluye los datos secretos (contrasenas).
     *
     * @return el objeto como texto
     */
    @Override
    public String toString() {
        return "Usuario [id=" + id
                + ", correo=" + correo
                + ", genero=" + genero
                + ", rol=" + rol
                + ", nombre=" + nombre
                + ", apellido=" + apellido
                + ", fotoPerfil=" + fotoPerfil
                + ", fechaNacimiento=" + fechaNacimiento
                + ", intereses=" + intereses
                + ", estudios=" + estudios
                + ", redes=" + redes
                + "]";
    }
}
