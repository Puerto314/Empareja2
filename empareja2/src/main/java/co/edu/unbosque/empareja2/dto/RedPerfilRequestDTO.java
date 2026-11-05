package co.edu.unbosque.empareja2.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Red social + nombre de usuario del estudiante en ella (tabla estudiante_red_social del modelo).
 */
public class RedPerfilRequestDTO {

    /** El identificador de la red social. */
    @NotNull(message = "La red social es obligatoria")
    private Long idRedSocial;

    /** El nombre de usuario del estudiante en esa red. */
    @NotBlank(message = "El nombre de usuario en la red social es obligatorio")
    @Size(max = 50, message = "El nombre de usuario en la red social es demasiado largo")
    private String nombreUsuario;

    /**
     * Crea un objeto RedPerfilRequestDTO vacio; los datos se asignan con los metodos set.
     */
    public RedPerfilRequestDTO() {
    }

    /**
     * Crea un objeto RedPerfilRequestDTO con todos sus datos.
     *
     * @param idRedSocial el identificador de la red social
     * @param nombreUsuario el nombre de usuario del estudiante en esa red
     */
    public RedPerfilRequestDTO(Long idRedSocial, String nombreUsuario) {
        this.idRedSocial = idRedSocial;
        this.nombreUsuario = nombreUsuario;
    }

    /**
     * Obtiene el identificador de la red social.
     *
     * @return el identificador de la red social
     */
    public Long getIdRedSocial() {
        return idRedSocial;
    }

    /**
     * Establece el identificador de la red social.
     *
     * @param idRedSocial el identificador de la red social
     */
    public void setIdRedSocial(Long idRedSocial) {
        this.idRedSocial = idRedSocial;
    }

    /**
     * Obtiene el nombre de usuario del estudiante en esa red.
     *
     * @return el nombre de usuario del estudiante en esa red
     */
    public String getNombreUsuario() {
        return nombreUsuario;
    }

    /**
     * Establece el nombre de usuario del estudiante en esa red.
     *
     * @param nombreUsuario el nombre de usuario del estudiante en esa red
     */
    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    /**
     * Devuelve una representacion en texto de este objeto para depuracion.
     *
     * @return el objeto como texto
     */
    @Override
    public String toString() {
        return "RedPerfilRequestDTO [idRedSocial=" + idRedSocial
                + ", nombreUsuario=" + nombreUsuario
                + "]";
    }
}
