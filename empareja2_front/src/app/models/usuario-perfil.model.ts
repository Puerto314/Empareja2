import { EstudioModel } from './estudio.model';
import { GeneroEnum } from './genero.enum';
import { RedPerfilModel } from './red-perfil.model';
import { RolEnum } from './rol.enum';
import { TipoDocumentoEnum } from './tipo-documento.enum';

// Refleja UsuarioPerfilDTO: el perfil completo del propio usuario.
export interface UsuarioPerfilModel {
  id: number;
  tipoDocumento: TipoDocumentoEnum;
  documento: string;
  correo: string;
  rol: RolEnum;
  genero: GeneroEnum | null;
  nombre: string;
  apellido: string;
  nombreCompleto: string;
  edad: number | null;
  fechaNacimiento: string | null;
  fotoPerfil: string | null;
  intereses: string[];
  estudios: EstudioModel[];
  redes: RedPerfilModel[];
  fechaCreacion: string;
}
