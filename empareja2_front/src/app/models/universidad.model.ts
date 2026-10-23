import { EstudianteModel } from './estudiante.model';

// El backend no tiene universidades como entidad: se arman agrupando "university" de los estudiantes.
export interface UniversidadModel {
  nombre: string;
  sigla: string;
  cantidad: number;
  vistaPrevia: EstudianteModel[];
}
