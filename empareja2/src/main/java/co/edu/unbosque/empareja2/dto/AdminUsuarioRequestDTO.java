package co.edu.unbosque.empareja2.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import co.edu.unbosque.empareja2.entity.Genero;
import co.edu.unbosque.empareja2.entity.Rol;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Datos que un administrador envia para crear o editar cualquier usuario
 * (estudiante o administrador). Aqui "correo" funciona como nombre de usuario y
 * no se exige formato de correo.
 *
 * La contrasena es obligatoria al crear; al editar puede omitirse para no
 * modificarla.
 */
public class AdminUsuarioRequestDTO {

    /** El usuario/correo con el que inicia sesion. */
    @NotBlank(message = "El usuario/correo es obligatorio")
    @Size(max = 100, message = "El usuario/correo es demasiado largo")
    private String correo;

    /** La contrasena en texto (obligatoria al crear, opcional al editar). */
    @Size(min = 8, max = 72, message = "La contrasena debe tener entre 8 y 72 caracteres")
    private String contrasena;

    /** El rol del usuario (si es null se asume ESTUDIANTE al crear). */
    private Rol rol;

    /** El genero del usuario. */
    private Genero genero;

    /** El nombre del usuario. */
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 50, message = "El nombre es demasiado largo")
    private String nombre;

    /** El apellido del usuario. */
    @NotBlank(message = "El apellido es obligatorio")
    @Size(max = 50, message = "El apellido es demasiado largo")
    private String apellido;

    /** La fecha de nacimiento (debe ser pasada). */
    @Past(message = "La fecha de nacimiento debe ser en el pasado")
    private LocalDate fechaNacimiento;

    /** La direccion http(s) de la foto de perfil (puede ser vacia). */
    @Size(max = 255, message = "La foto de perfil es demasiado larga")
    @Pattern(regexp = "^(https?://\\S+)?$", message = "La foto de perfil debe ser una url http:// o https://")
    private String fotoPerfil;

    /** Los intereses del usuario. */
    private Set<@NotBlank(message = "Un interes no puede estar vacio") @Size(max = 50, message = "Un interes es demasiado largo") String> intereses;

    /** La carrera y universidad del estudiante (a lo sumo una). */
    @Size(max = 1, message = "Solo se admite una carrera y universidad por estudiante")
    @Valid
    private List<EstudioRequestDTO> estudios;

    /** Las redes sociales del usuario. */
    @Valid
    private List<RedPerfilRequestDTO> redes;

    /**
     * Crea un objeto AdminUsuarioRequestDTO vacio; los datos se asignan con los metodos set.
     */
    public AdminUsuarioRequestDTO() {
    }

    /**
     * Crea un objeto AdminUsuarioRequestDTO con todos sus datos.
     *
     * @param correo el usuario/correo con el que inicia sesion
     * @param contrasena la contrasena en texto (obligatoria al crear, opcional al editar)
     * @param rol el rol del usuario (si es null se asume ESTUDIANTE al crear)
     * @param genero el genero del usuario
     * @param nombre el nombre del usuario
     * @param apellido el apellido del usuario
     * @param fechaNacimiento la fecha de nacimiento (debe ser pasada)
     * @param fotoPerfil la direccion http(s) de la foto de perfil (puede ser vacia)
     * @param intereses los intereses del usuario
     * @param estudios la carrera y universidad del estudiante (a lo sumo una)
     * @param redes las redes sociales del usuario
     */
    public AdminUsuarioRequestDTO(String correo, String contrasena, Rol rol, Genero genero, String nombre,
            String apellido, LocalDate fechaNacimiento, String fotoPerfil, Set<String> intereses,
            List<EstudioRequestDTO> estudios, List<RedPerfilRequestDTO> redes) {
        this.correo = correo;
        this.contrasena = contrasena;
        this.rol = rol;
        this.genero = genero;
        this.nombre = nombre;
        this.apellido = apellido;
        this.fechaNacimiento = fechaNacimiento;
        this.fotoPerfil = fotoPerfil;
        this.intereses = intereses;
        this.estudios = estudios;
        this.redes = redes;
    }

    /**
     * Obtiene el usuario/correo con el que inicia sesion.
     *
     * @return el usuario/correo con el que inicia sesion
     */
    public String getCorreo() {
        return correo;
    }

    /**
     * Establece el usuario/correo con el que inicia sesion.
     *
     * @param correo el usuario/correo con el que inicia sesion
     */
    public void setCorreo(String correo) {
        this.correo = correo;
    }

    /**
     * Obtiene la contrasena en texto (obligatoria al crear, opcional al editar).
     *
     * @return la contrasena en texto (obligatoria al crear, opcional al editar)
     */
    public String getContrasena() {
        return contrasena;
    }

    /**
     * Establece la contrasena en texto (obligatoria al crear, opcional al editar).
     *
     * @param contrasena la contrasena en texto (obligatoria al crear, opcional al editar)
     */
    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    /**
     * Obtiene el rol del usuario (si es null se asume ESTUDIANTE al crear).
     *
     * @return el rol del usuario (si es null se asume ESTUDIANTE al crear)
     */
    public Rol getRol() {
        return rol;
    }

    /**
     * Establece el rol del usuario (si es null se asume ESTUDIANTE al crear).
     *
     * @param rol el rol del usuario (si es null se asume ESTUDIANTE al crear)
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
     * Obtiene la fecha de nacimiento (debe ser pasada).
     *
     * @return la fecha de nacimiento (debe ser pasada)
     */
    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    /**
     * Establece la fecha de nacimiento (debe ser pasada).
     *
     * @param fechaNacimiento la fecha de nacimiento (debe ser pasada)
     */
    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    /**
     * Obtiene la direccion http(s) de la foto de perfil (puede ser vacia).
     *
     * @return la direccion http(s) de la foto de perfil (puede ser vacia)
     */
    public String getFotoPerfil() {
        return fotoPerfil;
    }

    /**
     * Establece la direccion http(s) de la foto de perfil (puede ser vacia).
     *
     * @param fotoPerfil la direccion http(s) de la foto de perfil (puede ser vacia)
     */
    public void setFotoPerfil(String fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }

    /**
     * Obtiene los intereses del usuario.
     *
     * @return los intereses del usuario
     */
    public Set<String> getIntereses() {
        return intereses;
    }

    /**
     * Establece los intereses del usuario.
     *
     * @param intereses los intereses del usuario
     */
    public void setIntereses(Set<String> intereses) {
        this.intereses = intereses;
    }

    /**
     * Obtiene la carrera y universidad del estudiante (a lo sumo una).
     *
     * @return la carrera y universidad del estudiante (a lo sumo una)
     */
    public List<EstudioRequestDTO> getEstudios() {
        return estudios;
    }

    /**
     * Establece la carrera y universidad del estudiante (a lo sumo una).
     *
     * @param estudios la carrera y universidad del estudiante (a lo sumo una)
     */
    public void setEstudios(List<EstudioRequestDTO> estudios) {
        this.estudios = estudios;
    }

    /**
     * Obtiene las redes sociales del usuario.
     *
     * @return las redes sociales del usuario
     */
    public List<RedPerfilRequestDTO> getRedes() {
        return redes;
    }

    /**
     * Establece las redes sociales del usuario.
     *
     * @param redes las redes sociales del usuario
     */
    public void setRedes(List<RedPerfilRequestDTO> redes) {
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
        return "AdminUsuarioRequestDTO [correo=" + correo
                + ", rol=" + rol
                + ", genero=" + genero
                + ", nombre=" + nombre
                + ", apellido=" + apellido
                + ", fechaNacimiento=" + fechaNacimiento
                + ", fotoPerfil=" + fotoPerfil
                + ", intereses=" + intereses
                + ", estudios=" + estudios
                + ", redes=" + redes
                + "]";
    }
}
