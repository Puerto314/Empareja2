import { GeneroEnum } from './genero.enum';

export interface EstudioRequestModel {
  idCarrera: number;
  idUniversidad: number;
  semestre: number;
}

export interface RedPerfilRequestModel {
  tipoRedSocial: string;
  urlPerfil: string;
}

export interface RegistroRequestModel {
  correo: string;
  contrasena: string;
  genero: GeneroEnum;
  nombre: string;
  apellido: string;
  fechaNacimiento: string;
  fotoPerfil?: string;
  intereses?: string[];
  estudios: EstudioRequestModel[];
  redes?: RedPerfilRequestModel[];
}
