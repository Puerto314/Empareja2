import { PerfilModel } from './perfil.model';

export interface AuthResponseModel {
  token: string;
  tokenType: string;
  expiresInMs: number;
  usuario: PerfilModel;
}
