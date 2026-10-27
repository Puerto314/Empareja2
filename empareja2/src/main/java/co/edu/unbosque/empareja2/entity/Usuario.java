package co.edu.unbosque.empareja2.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * USUARIO. Atributos del modelo: tipo_documento, documento, correo,
 * contrasena_hash, genero, rol, nombre_completo (compuesto: nombre + apellido),
 * foto_perfil, edad (derivado de fecha_nacimiento), intereses (multivaluado) y
 * fecha_nacimiento.
 *
 * Notas de implementacion:
 * - "nombre_completo" y "edad" NO se guardan: se calculan (getNombreCompleto y
 * getEdad).
 * - "intereses" (multivaluado) se guarda en la tabla aparte usuario_interes.
 * - (tipo_documento, documento) es unico y es el identificador natural; se usa
 * ademas una llave sustituta "id_usuario" para simplificar las llaves foraneas
 * de las relaciones.
 */
public class Usuario {

    /** Edad minima para registrarse en el catalogo. */
    public static final int EDAD_MINIMA = 18;

    private Long id;

    private TipoDocumento tipoDocumento;

    private String documento;

    private String correo;

    private String contrasenaHash;

    private Genero genero;

    private Rol rol = Rol.ESTUDIANTE;

    private String nombre;

    private String apellido;

    private String fotoPerfil;

    private LocalDate fechaNacimiento;

    private Set<String> intereses = new LinkedHashSet<>();

    private List<Estudio> estudios = new ArrayList<>();

    private List<Tiene> redes = new ArrayList<>();

    private LocalDateTime fechaCreacion;

    public Usuario() {
    }

    /** Atributo compuesto: nombre + apellido. */
    public String getNombreCompleto() {
        String n = nombre == null ? "" : nombre.trim();
        String a = apellido == null ? "" : apellido.trim();
        return (n + " " + a).trim();
    }

    /** Atributo derivado: se calcula a partir de fecha_nacimiento. */
    public Integer getEdad() {
        return calcularEdad(fechaNacimiento, LocalDate.now());
    }

    public static Integer calcularEdad(LocalDate fechaNacimiento, LocalDate hoy) {
        if (fechaNacimiento == null) {
            return null;
        }
        return Period.between(fechaNacimiento, hoy).getYears();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TipoDocumento getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(TipoDocumento tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContrasenaHash() {
        return contrasenaHash;
    }

    public void setContrasenaHash(String contrasenaHash) {
        this.contrasenaHash = contrasenaHash;
    }

    public Genero getGenero() {
        return genero;
    }

    public void setGenero(Genero genero) {
        this.genero = genero;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getFotoPerfil() {
        return fotoPerfil;
    }

    public void setFotoPerfil(String fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public Set<String> getIntereses() {
        return intereses;
    }

    public void setIntereses(Set<String> intereses) {
        this.intereses = intereses;
    }

    public List<Estudio> getEstudios() {
        return estudios;
    }

    public void setEstudios(List<Estudio> estudios) {
        this.estudios = estudios;
    }

    public List<Tiene> getRedes() {
        return redes;
    }

    public void setRedes(List<Tiene> redes) {
        this.redes = redes;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
}
