package co.edu.unbosque.empareja2.service;

import co.edu.unbosque.empareja2.dto.AuthResponseDTO;
import co.edu.unbosque.empareja2.dto.LoginRequestDTO;
import co.edu.unbosque.empareja2.dto.RegistroRequestDTO;

/**
 * Contrato para registro e inicio de sesion.
 */
public interface AuthService {

    /**
     * Registra un estudiante nuevo y le abre sesion.
     *
     * @param request los datos de registro
     * @return el token y el perfil del usuario creado
     */
    AuthResponseDTO registrar(RegistroRequestDTO request);

    /**
     * Inicia sesion con correo y contrasena.
     *
     * @param request las credenciales
     * @return el token y el perfil del usuario
     */
    AuthResponseDTO iniciarSesion(LoginRequestDTO request);
}
