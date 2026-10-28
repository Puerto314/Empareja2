import { PerfilModel } from './perfil.model';

// Refleja AuthResponseDTO: respuesta de /api/auth/login y /api/auth/registro.
export interface AuthResponseModel {
  token: string;
  tokenType: string;
  expiresInMs: number;
  usuario: PerfilModel;
}
