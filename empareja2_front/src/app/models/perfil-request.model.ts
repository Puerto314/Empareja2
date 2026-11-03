import { EstudioRequestModel, RedPerfilRequestModel } from './registro-request.model';
import { GeneroEnum } from './genero.enum';
import { RolEnum } from './rol.enum';

// ActualizarPerfilRequestDTO (PUT /api/usuarios/me). Las listas REEMPLAZAN a las actuales.
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

// AdminUsuarioRequestDTO (POST/PUT /api/admin/usuarios). La contraseña es opcional al editar.
export interface AdminUsuarioRequestModel {
  correo: string;
  contrasena?: string;
  rol?: RolEnum;
  genero?: GeneroEnum | null;
  nombre: string;
  apellido: string;
  fechaNacimiento?: string; // obligatoria para ESTUDIANTE; ADMIN sin fecha queda en 2000-01-01
  fotoPerfil?: string;
  intereses?: string[];
  estudios?: EstudioRequestModel[]; // máximo 1; obligatorio si el rol es ESTUDIANTE
  redes?: RedPerfilRequestModel[];
}
