import { EstudioModel } from './estudio.model';
import { GeneroEnum } from './genero.enum';
import { RedPerfilModel } from './red-perfil.model';

// Refleja UsuarioPublicoDTO: información NO sensible visible para cualquier usuario autenticado.
export interface EstudianteModel {
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
