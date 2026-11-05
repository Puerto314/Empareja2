package co.edu.unbosque.empareja2.dto;

import java.time.LocalDate;
import java.util.List;

import co.edu.unbosque.empareja2.entity.Genero;
import co.edu.unbosque.empareja2.entity.Rol;

/**
 * Perfil completo de un usuario, incluyendo sus datos privados (correo,
 * fecha de nacimiento, rol). Solo se devuelve al propio dueno o a un
 * administrador, nunca a terceros. Nunca incluye la contrasena ni su hash.
 */
public class UsuarioPerfilDTO {

    /** El identificador del usuario. */
    private Long id;

    /** El correo del usuario. */
    private String correo;

    /** El rol del usuario. */
    private Rol rol;

    /** El genero del usuario. */
    private Genero genero;

    /** El nombre del usuario. */
    private String nombre;

    /** El apellido del usuario. */
    private String apellido;

    /** El nombre y apellido juntos. */
    private String nombreCompleto;

    /** La edad calculada a partir de la fecha de nacimiento. */
    private Integer edad;

    /** La fecha de nacimiento. */
    private LocalDate fechaNacimiento;

    /** La foto de perfil. */
    private String fotoPerfil;

    /** Los intereses del usuario, ordenados. */
    private List<String> intereses;

    /** Los estudios del usuario. */
    private List<EstudioDTO> estudios;

    /** Las redes sociales del usuario. */
    private List<RedPerfilDTO> redes;

    /**
     * Crea un objeto UsuarioPerfilDTO vacio; los datos se asignan con los metodos set.
     */
    public UsuarioPerfilDTO() {
    }

    /**
     * Crea un objeto UsuarioPerfilDTO con todos sus datos.
     *
     * @param id el identificador del usuario
     * @param correo el correo del usuario
     * @param rol el rol del usuario
     * @param genero el genero del usuario
     * @param nombre el nombre del usuario
     * @param apellido el apellido del usuario
     * @param nombreCompleto el nombre y apellido juntos
     * @param edad la edad calculada a partir de la fecha de nacimiento
     * @param fechaNacimiento la fecha de nacimiento
     * @param fotoPerfil la foto de perfil
     * @param intereses los intereses del usuario, ordenados
     * @param estudios los estudios del usuario
     * @param redes las redes sociales del usuario
     */
    public UsuarioPerfilDTO(Long id, String correo, Rol rol, Genero genero, String nombre, String apellido,
            String nombreCompleto, Integer edad, LocalDate fechaNacimiento, String fotoPerfil,
            List<String> intereses, List<EstudioDTO> estudios, List<RedPerfilDTO> redes) {
        this.id = id;
        this.correo = correo;
        this.rol = rol;
        this.genero = genero;
        this.nombre = nombre;
        this.apellido = apellido;
        this.nombreCompleto = nombreCompleto;
        this.edad = edad;
        this.fechaNacimiento = fechaNacimiento;
        this.fotoPerfil = fotoPerfil;
        this.intereses = intereses;
        this.estudios = estudios;
        this.redes = redes;
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
     * Obtiene el correo del usuario.
     *
     * @return el correo del usuario
     */
    public String getCorreo() {
        return correo;
    }

    /**
     * Establece el correo del usuario.
     *
     * @param correo el correo del usuario
     */
    public void setCorreo(String correo) {
        this.correo = correo;
    }

    /**
     * Obtiene el rol del usuario.
     *
     * @return el rol del usuario
     */
    public Rol getRol() {
        return rol;
    }

    /**
     * Establece el rol del usuario.
     *
     * @param rol el rol del usuario
     */
    public void setRol(Rol rol) {
        this.rol = rol;
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
     * Obtiene el nombre y apellido juntos.
     *
     * @return el nombre y apellido juntos
     */
    public String getNombreCompleto() {
        return nombreCompleto;
    }

    /**
     * Establece el nombre y apellido juntos.
     *
     * @param nombreCompleto el nombre y apellido juntos
     */
    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    /**
     * Obtiene la edad calculada a partir de la fecha de nacimiento.
     *
     * @return la edad calculada a partir de la fecha de nacimiento
     */
    public Integer getEdad() {
        return edad;
    }

    /**
     * Establece la edad calculada a partir de la fecha de nacimiento.
     *
     * @param edad la edad calculada a partir de la fecha de nacimiento
     */
    public void setEdad(Integer edad) {
        this.edad = edad;
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
     * Obtiene la foto de perfil.
     *
     * @return la foto de perfil
     */
    public String getFotoPerfil() {
        return fotoPerfil;
    }

    /**
     * Establece la foto de perfil.
     *
     * @param fotoPerfil la foto de perfil
     */
    public void setFotoPerfil(String fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }

    /**
     * Obtiene los intereses del usuario, ordenados.
     *
     * @return los intereses del usuario, ordenados
     */
    public List<String> getIntereses() {
        return intereses;
    }

    /**
     * Establece los intereses del usuario, ordenados.
     *
     * @param intereses los intereses del usuario, ordenados
     */
    public void setIntereses(List<String> intereses) {
        this.intereses = intereses;
    }

    /**
     * Obtiene los estudios del usuario.
     *
     * @return los estudios del usuario
     */
    public List<EstudioDTO> getEstudios() {
        return estudios;
    }

    /**
     * Establece los estudios del usuario.
     *
     * @param estudios los estudios del usuario
     */
    public void setEstudios(List<EstudioDTO> estudios) {
        this.estudios = estudios;
    }

    /**
     * Obtiene las redes sociales del usuario.
     *
     * @return las redes sociales del usuario
     */
    public List<RedPerfilDTO> getRedes() {
        return redes;
    }

    /**
     * Establece las redes sociales del usuario.
     *
     * @param redes las redes sociales del usuario
     */
    public void setRedes(List<RedPerfilDTO> redes) {
        this.redes = redes;
    }

    /**
     * Devuelve una representacion en texto de este objeto para depuracion.
     *
     * @return el objeto como texto
     */
    @Override
    public String toString() {
        return "UsuarioPerfilDTO [id=" + id
                + ", correo=" + correo
                + ", rol=" + rol
                + ", genero=" + genero
                + ", nombre=" + nombre
                + ", apellido=" + apellido
                + ", nombreCompleto=" + nombreCompleto
                + ", edad=" + edad
                + ", fechaNacimiento=" + fechaNacimiento
                + ", fotoPerfil=" + fotoPerfil
                + ", intereses=" + intereses
                + ", estudios=" + estudios
                + ", redes=" + redes
                + "]";
    }
}
