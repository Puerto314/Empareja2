// DTOs de catálogos (CarreraDTO, RedSocialDTO, OfreceDTO) y los request de administración.
export interface CarreraModel { idCarrera: number; nombre: string; }
export interface RedSocialModel { idRedSocial: number; nombreRedSocial: string; url: string; }
export interface OfertaModel {
  idCarrera: number; carrera: string; idUniversidad: number; universidad: string; facultad: string;
}

export interface CiudadRequestModel { nombre: string; departamento: string; }
export interface UniversidadRequestModel {
  nombre: string; sigla: string; calle?: string; barrio?: string; numeroDireccion?: string; idCiudad: number;
}
export interface CarreraRequestModel { nombre: string; }
export interface RedSocialRequestModel { nombreRedSocial: string; url: string; }
export interface OfertaRequestModel { idCarrera: number; idUniversidad: number; facultad: string; }
