package co.edu.unbosque.empareja2.service;

import co.edu.unbosque.empareja2.dto.request.LoginRequestDTO;
import co.edu.unbosque.empareja2.dto.request.RegistroRequestDTO;
import co.edu.unbosque.empareja2.dto.response.AuthResponseDTO;

/**
 * Contrato para registro e inicio de sesion.
 */
public interface AuthService {

    AuthResponseDTO registrar(RegistroRequestDTO request);

    AuthResponseDTO iniciarSesion(LoginRequestDTO request);
}
