export interface CarreraModel { idCarrera: number; nombre: string; facultad: string; }
export interface OfertaModel {
  idCarrera: number; carrera: string; idUniversidad: number; universidad: string; facultad: string;
}

export interface CiudadRequestModel { nombre: string; departamento: string; }
export interface UniversidadRequestModel {
  nombre: string; sigla: string; calle: string; barrio: string; idCiudad: number;
}
export interface CarreraRequestModel { nombre: string; facultad: string; }
export interface OfertaRequestModel { idCarrera: number; idUniversidad: number; }
