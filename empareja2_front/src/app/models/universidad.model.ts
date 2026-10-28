// Refleja UniversidadDTO (y CiudadDTO).
export interface CiudadModel {
  idCiudad: number;
  nombre: string;
  departamento: string;
}

<<<<<<< HEAD
// Refleja CiudadDTO.
export interface CiudadModel {
  idCiudad: number;
  nombre: string;
  departamento: string;
}

// Refleja UniversidadDTO (GET /api/catalogos/universidades).
=======
>>>>>>> main
export interface UniversidadModel {
  idUniversidad: number;
  nombre: string;
  sigla: string;
<<<<<<< HEAD
  calle: string | null;
  barrio: string | null;
  numeroDireccion: string | null;
  ciudad: CiudadModel | null;
}

// Universidad + cuántos estudiantes tiene y una vista previa. El backend no entrega
// conteos, así que se calculan en el front cruzando universidades con el catálogo de usuarios.
export interface UniversidadResumenModel extends UniversidadModel {
  cantidad: number;
  vistaPrevia: EstudianteModel[];
=======
  calle?: string | null;
  barrio?: string | null;
  numeroDireccion?: string | null;
  ciudad?: CiudadModel | null;
>>>>>>> main
}

// Resultado de la pantalla de universidades. totalEstudiantes cuenta personas distintas
// (un estudiante con carreras en dos universidades aparece en ambas, pero se cuenta una vez).
export interface ResumenUniversidadesModel {
  universidades: UniversidadResumenModel[];
  totalEstudiantes: number;
}
