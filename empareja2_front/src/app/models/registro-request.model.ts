import { GeneroEnum } from './genero.enum';
import { TipoDocumentoEnum } from './tipo-documento.enum';

export interface EstudioRequestModel {
  idCarrera: number;
  idUniversidad: number;
  semestre: number;
}

<<<<<<< HEAD
export interface RedPerfilRequestModel {
  idRedSocial: number;
  urlPerfil: string;
  alias?: string;
}

// Refleja RegistroRequestDTO. La contraseña debe tener entre 8 y 72 caracteres y
// se requiere al menos un estudio (carrera + universidad + semestre).
=======
// Refleja RegistroRequestDTO.
>>>>>>> main
export interface RegistroRequestModel {
  correo: string;
  contrasena: string;
  tipoDocumento: TipoDocumentoEnum;
  documento: string;
<<<<<<< HEAD
  genero?: GeneroEnum;
  nombre: string;
  apellido: string;
  fechaNacimiento: string; // AAAA-MM-DD
  fotoPerfil?: string;
  intereses?: string[];
  estudios: EstudioRequestModel[];
  redes?: RedPerfilRequestModel[];
=======
  genero?: GeneroEnum | null;
  nombre: string;
  apellido: string;
  fechaNacimiento: string;
  fotoPerfil?: string | null;
  intereses?: string[];
  estudios: EstudioRequestModel[];
>>>>>>> main
}
