package co.edu.unbosque.empareja2.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Credenciales para iniciar sesion. No se exige formato de correo porque un
 * administrador puede tener un usuario simple (ej: "Puerto314").
 */
public class LoginRequestDTO {

    /** El usuario/correo. */
    @NotBlank(message = "El usuario/correo es obligatorio")
    private String correo;

    /** La contrasena en texto. */
    @NotBlank(message = "La contrasena es obligatoria")
    private String contrasena;

    /**
     * Crea un objeto LoginRequestDTO vacio; los datos se asignan con los metodos set.
     */
    public LoginRequestDTO() {
    }

    /**
     * Crea un objeto LoginRequestDTO con todos sus datos.
     *
     * @param correo el usuario/correo
     * @param contrasena la contrasena en texto
     */
    public LoginRequestDTO(String correo, String contrasena) {
        this.correo = correo;
        this.contrasena = contrasena;
    }

    /**
     * Obtiene el usuario/correo.
     *
     * @return el usuario/correo
     */
    public String getCorreo() {
        return correo;
    }

    /**
     * Establece el usuario/correo.
     *
     * @param correo el usuario/correo
     */
    public void setCorreo(String correo) {
        this.correo = correo;
    }

    /**
     * Obtiene la contrasena en texto.
     *
     * @return la contrasena en texto
     */
    public String getContrasena() {
        return contrasena;
    }

    /**
     * Establece la contrasena en texto.
     *
     * @param contrasena la contrasena en texto
     */
    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    /**
     * Devuelve una representacion en texto de este objeto para depuracion.
     * No incluye los datos secretos (contrasenas).
     *
     * @return el objeto como texto
     */
    @Override
    public String toString() {
        return "LoginRequestDTO [correo=" + correo
                + "]";
    }
}
