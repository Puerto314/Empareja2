package co.edu.unbosque.empareja2.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import co.edu.unbosque.empareja2.entity.Genero;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Datos que un usuario autenticado puede actualizar de su propio perfil. No
 * incluye correo, documento ni contrasena. Las listas (intereses, estudios,
 * redes) REEMPLAZAN a las actuales.
 */
public class ActualizarPerfilRequestDTO {

    /** El nombre del usuario. */
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 50, message = "El nombre es demasiado largo")
    private String nombre;

    /** El apellido del usuario. */
    @NotBlank(message = "El apellido es obligatorio")
    @Size(max = 50, message = "El apellido es demasiado largo")
    private String apellido;

    /** El genero del usuario. */
    @NotNull(message = "El genero es obligatorio")
    private Genero genero;

    /** La fecha de nacimiento (debe ser pasada). */
    @NotNull(message = "La fecha de nacimiento es obligatoria")
    @Past(message = "La fecha de nacimiento debe ser en el pasado")
    private LocalDate fechaNacimiento;

    /** La direccion http(s) de la foto de perfil (puede ser vacia). */
    @Size(max = 255, message = "La foto de perfil es demasiado larga")
    @Pattern(regexp = "^(https?://\\S+)?$", message = "La foto de perfil debe ser una url http:// o https://")
    private String fotoPerfil;

    /** Los intereses del usuario (reemplazan a los actuales). */
    private Set<@NotBlank(message = "Un interes no puede estar vacio") @Size(max = 50, message = "Un interes es demasiado largo") String> intereses;

    /** La carrera y universidad del usuario (exactamente una). */
    @NotEmpty(message = "Debes indicar tu carrera y universidad")
    @Size(max = 1, message = "Solo puedes indicar una carrera y universidad")
    @Valid
    private List<EstudioRequestDTO> estudios;

    /** Las redes sociales del usuario. */
    @Valid
    private List<RedPerfilRequestDTO> redes;

    /**
     * Crea un objeto ActualizarPerfilRequestDTO vacio; los datos se asignan con los metodos set.
     */
    public ActualizarPerfilRequestDTO() {
    }

    /**
     * Crea un objeto ActualizarPerfilRequestDTO con todos sus datos.
     *
     * @param nombre el nombre del usuario
     * @param apellido el apellido del usuario
     * @param genero el genero del usuario
     * @param fechaNacimiento la fecha de nacimiento (debe ser pasada)
     * @param fotoPerfil la direccion http(s) de la foto de perfil (puede ser vacia)
     * @param intereses los intereses del usuario (reemplazan a los actuales)
     * @param estudios la carrera y universidad del usuario (exactamente una)
     * @param redes las redes sociales del usuario
     */
    public ActualizarPerfilRequestDTO(String nombre, String apellido, Genero genero, LocalDate fechaNacimiento,
            String fotoPerfil, Set<String> intereses, List<EstudioRequestDTO> estudios,
            List<RedPerfilRequestDTO> redes) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.genero = genero;
        this.fechaNacimiento = fechaNacimiento;
        this.fotoPerfil = fotoPerfil;
        this.intereses = intereses;
        this.estudios = estudios;
        this.redes = redes;
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
     * Obtiene los intereses del usuario (reemplazan a los actuales).
     *
     * @return los intereses del usuario (reemplazan a los actuales)
     */
    public Set<String> getIntereses() {
        return intereses;
    }

    /**
     * Establece los intereses del usuario (reemplazan a los actuales).
     *
     * @param intereses los intereses del usuario (reemplazan a los actuales)
     */
    public void setIntereses(Set<String> intereses) {
        this.intereses = intereses;
    }

    /**
     * Obtiene la carrera y universidad del usuario (exactamente una).
     *
     * @return la carrera y universidad del usuario (exactamente una)
     */
    public List<EstudioRequestDTO> getEstudios() {
        return estudios;
    }

    /**
     * Establece la carrera y universidad del usuario (exactamente una).
     *
     * @param estudios la carrera y universidad del usuario (exactamente una)
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
     *
     * @return el objeto como texto
     */
    @Override
    public String toString() {
        return "ActualizarPerfilRequestDTO [nombre=" + nombre
                + ", apellido=" + apellido
                + ", genero=" + genero
                + ", fechaNacimiento=" + fechaNacimiento
                + ", fotoPerfil=" + fotoPerfil
                + ", intereses=" + intereses
                + ", estudios=" + estudios
                + ", redes=" + redes
                + "]";
    }
}
