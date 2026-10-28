import { EstudioModel } from './estudio.model';
import { GeneroEnum } from './genero.enum';
import { RedPerfilModel } from './red-perfil.model';

// Refleja UsuarioPublicoDTO: lo que ve cualquier usuario autenticado.
export interface UsuarioPublicoModel {
  id: number;
  nombre: string;
  apellido: string;
  nombreCompleto: string;
  edad: number | null;
  genero: GeneroEnum | null;
  fotoPerfil: string | null;
  intereses: string[];
  estudios: EstudioModel[];
  redes: RedPerfilModel[];
  totalMeGusta: number;
  meGustaDelUsuarioActual: boolean;
}
