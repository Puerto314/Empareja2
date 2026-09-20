package co.edu.unbosque.empareja2.dto.request;

import jakarta.validation.constraints.NotBlank;

/**
 * Credenciales enviadas para iniciar sesion.
 *
 * No se exige formato de correo aqui: los estudiantes inician sesion con su
 * email, pero un administrador puede iniciar sesion con un nombre de usuario
 * simple (ej: "Puerto314").
 */
public class LoginRequestDTO {

	@NotBlank(message = "El usuario/correo es obligatorio")
	private String email;

	@NotBlank(message = "La contrasena es obligatoria")
	private String password;

	public LoginRequestDTO() {
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
