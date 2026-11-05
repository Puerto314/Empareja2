package co.edu.unbosque.empareja2.dto;

import java.util.List;

import co.edu.unbosque.empareja2.entity.Genero;

/**
 * Informacion NO sensible de un usuario, visible para cualquier usuario
 * autenticado. Deliberadamente NO incluye: correo, contrasena, tipo de
 * documento, documento, fecha de nacimiento ni rol. Se muestra la edad (derivada)
 * pero no la fecha exacta de nacimiento.
 */
public class UsuarioPublicoDTO {

    /** El identificador del usuario. */
    private Long id;

    /** El nombre del usuario. */
    private String nombre;

    /** El apellido del usuario. */
    private String apellido;

    /** El nombre y apellido juntos. */
    private String nombreCompleto;

    /** La edad calculada a partir de la fecha de nacimiento. */
    private Integer edad;

    /** El genero del usuario. */
    private Genero genero;

    /** La foto de perfil. */
    private String fotoPerfil;

    /** Los intereses del usuario, ordenados. */
    private List<String> intereses;

    /** Los estudios del usuario. */
    private List<EstudioDTO> estudios;

    /** Las redes sociales del usuario. */
    private List<RedPerfilDTO> redes;

    /** El total de me gusta que ha recibido. */
    private long totalMeGusta;

    /** True si el usuario que consulta ya le dio me gusta. */
    private boolean meGustaDelUsuarioActual;

    /**
     * Crea un objeto UsuarioPublicoDTO vacio; los datos se asignan con los metodos set.
     */
    public UsuarioPublicoDTO() {
    }

    /**
     * Crea un objeto UsuarioPublicoDTO con todos sus datos.
     *
     * @param id el identificador del usuario
     * @param nombre el nombre del usuario
     * @param apellido el apellido del usuario
     * @param nombreCompleto el nombre y apellido juntos
     * @param edad la edad calculada a partir de la fecha de nacimiento
     * @param genero el genero del usuario
     * @param fotoPerfil la foto de perfil
     * @param intereses los intereses del usuario, ordenados
     * @param estudios los estudios del usuario
     * @param redes las redes sociales del usuario
     * @param totalMeGusta el total de me gusta que ha recibido
     * @param meGustaDelUsuarioActual true si el usuario que consulta ya le dio me gusta
     */
    public UsuarioPublicoDTO(Long id, String nombre, String apellido, String nombreCompleto, Integer edad,
            Genero genero, String fotoPerfil, List<String> intereses, List<EstudioDTO> estudios,
            List<RedPerfilDTO> redes, long totalMeGusta, boolean meGustaDelUsuarioActual) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.nombreCompleto = nombreCompleto;
        this.edad = edad;
        this.genero = genero;
        this.fotoPerfil = fotoPerfil;
        this.intereses = intereses;
        this.estudios = estudios;
        this.redes = redes;
        this.totalMeGusta = totalMeGusta;
        this.meGustaDelUsuarioActual = meGustaDelUsuarioActual;
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
     * Obtiene el total de me gusta que ha recibido.
     *
     * @return el total de me gusta que ha recibido
     */
    public long getTotalMeGusta() {
        return totalMeGusta;
    }

    /**
     * Establece el total de me gusta que ha recibido.
     *
     * @param totalMeGusta el total de me gusta que ha recibido
     */
    public void setTotalMeGusta(long totalMeGusta) {
        this.totalMeGusta = totalMeGusta;
    }

    /**
     * Obtiene true si el usuario que consulta ya le dio me gusta.
     *
     * @return true si el usuario que consulta ya le dio me gusta
     */
    public boolean isMeGustaDelUsuarioActual() {
        return meGustaDelUsuarioActual;
    }

    /**
     * Establece true si el usuario que consulta ya le dio me gusta.
     *
     * @param meGustaDelUsuarioActual true si el usuario que consulta ya le dio me gusta
     */
    public void setMeGustaDelUsuarioActual(boolean meGustaDelUsuarioActual) {
        this.meGustaDelUsuarioActual = meGustaDelUsuarioActual;
    }

    /**
     * Devuelve una representacion en texto de este objeto para depuracion.
     *
     * @return el objeto como texto
     */
    @Override
    public String toString() {
        return "UsuarioPublicoDTO [id=" + id
                + ", nombre=" + nombre
                + ", apellido=" + apellido
                + ", nombreCompleto=" + nombreCompleto
                + ", edad=" + edad
                + ", genero=" + genero
                + ", fotoPerfil=" + fotoPerfil
                + ", intereses=" + intereses
                + ", estudios=" + estudios
                + ", redes=" + redes
                + ", totalMeGusta=" + totalMeGusta
                + ", meGustaDelUsuarioActual=" + meGustaDelUsuarioActual
                + "]";
    }
}
