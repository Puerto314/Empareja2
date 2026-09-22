package co.edu.unbosque.empareja2.service;

import co.edu.unbosque.empareja2.dto.request.LoginRequestDTO;
import co.edu.unbosque.empareja2.dto.request.RegisterRequestDTO;
import co.edu.unbosque.empareja2.dto.response.AuthResponseDTO;

/**
 * Contrato para las operaciones de registro e inicio de sesion.
 */
public interface AuthService {

	AuthResponseDTO register(RegisterRequestDTO request);

	AuthResponseDTO login(LoginRequestDTO request);
}
