import { UsuarioPerfilModel } from './usuario-perfil.model';

// Refleja AuthResponseDTO.
export interface AuthResponseModel {
  token: string;
  tokenType: string;
  expiresInMs: number;
  usuario: UsuarioPerfilModel;
}
