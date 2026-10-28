<<<<<<< HEAD
import { PerfilModel } from './perfil.model';

// Refleja AuthResponseDTO: respuesta de /api/auth/login y /api/auth/registro.
=======
import { UsuarioPerfilModel } from './usuario-perfil.model';

// Refleja AuthResponseDTO.
>>>>>>> main
export interface AuthResponseModel {
  token: string;
  tokenType: string;
  expiresInMs: number;
<<<<<<< HEAD
  usuario: PerfilModel;
=======
  usuario: UsuarioPerfilModel;
>>>>>>> main
}
