import { UniversidadModel } from './universidad.model';
import { UsuarioPublicoModel } from './usuario-publico.model';

// Dato armado en el front: la universidad con cuántos estudiantes tiene y algunos para la vista previa.
export interface UniversidadResumenModel {
  universidad: UniversidadModel;
  cantidad: number;
  vistaPrevia: UsuarioPublicoModel[];
}
