import { EstudianteModel } from './estudiante.model';

export interface CiudadModel {
  idCiudad: number;
  nombre: string;
  departamento: string;
}

export interface UniversidadModel {
  idUniversidad: number;
  nombre: string;
  sigla: string;
  calle: string;
  barrio: string;
  ciudad: CiudadModel | null;
}

export interface UniversidadResumenModel extends UniversidadModel {
  cantidad: number;
  vistaPrevia: EstudianteModel[];
}

export interface ResumenUniversidadesModel {
  universidades: UniversidadResumenModel[];
  totalEstudiantes: number;
}
