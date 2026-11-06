import { EstudioRequestModel, RedPerfilRequestModel } from './registro-request.model';
import { GeneroEnum } from './genero.enum';
import { RolEnum } from './rol.enum';

export interface ActualizarPerfilRequestModel {
  nombre: string;
  apellido: string;
  genero?: GeneroEnum | null;
  fechaNacimiento: string;
  fotoPerfil?: string;
  intereses?: string[];
  estudios: EstudioRequestModel[];
  redes?: RedPerfilRequestModel[];
}

export interface AdminUsuarioRequestModel {
  correo: string;
  contrasena?: string;
  rol?: RolEnum;
  genero?: GeneroEnum | null;
  nombre: string;
  apellido: string;
  fechaNacimiento?: string;
  fotoPerfil?: string;
  intereses?: string[];
  estudios?: EstudioRequestModel[];
  redes?: RedPerfilRequestModel[];
}
