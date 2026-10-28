// Refleja UniversidadDTO (y CiudadDTO).
export interface CiudadModel {
  idCiudad: number;
  nombre: string;
  departamento: string;
}

export interface UniversidadModel {
  idUniversidad: number;
  nombre: string;
  sigla: string;
  calle?: string | null;
  barrio?: string | null;
  numeroDireccion?: string | null;
  ciudad?: CiudadModel | null;
}
