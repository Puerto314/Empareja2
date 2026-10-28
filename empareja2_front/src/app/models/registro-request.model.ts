import { GeneroEnum } from './genero.enum';
import { TipoDocumentoEnum } from './tipo-documento.enum';

export interface EstudioRequestModel {
  idCarrera: number;
  idUniversidad: number;
  semestre: number;
}

// Refleja RegistroRequestDTO.
export interface RegistroRequestModel {
  correo: string;
  contrasena: string;
  tipoDocumento: TipoDocumentoEnum;
  documento: string;
  genero?: GeneroEnum | null;
  nombre: string;
  apellido: string;
  fechaNacimiento: string;
  fotoPerfil?: string | null;
  intereses?: string[];
  estudios: EstudioRequestModel[];
}
