import { UniversidadModel } from '../models/universidad.model';
import { UsuarioPublicoModel } from '../models/usuario-publico.model';

// Datos de ejemplo compartidos por los specs.
export const UNIVERSIDAD_EJEMPLO: UniversidadModel = { idUniversidad: 1, nombre: 'Universidad Uno', sigla: 'UU' };
export const UNIVERSIDAD_VACIA_EJEMPLO: UniversidadModel = { idUniversidad: 2, nombre: 'Universidad Dos', sigla: 'UD' };

export const USUARIO_EJEMPLO: UsuarioPublicoModel = {
  id: 1, nombre: 'Ana', apellido: 'Prueba', nombreCompleto: 'Ana Prueba', edad: 21, genero: null, fotoPerfil: null,
  intereses: ['Café'], redes: [], totalMeGusta: 3, meGustaDelUsuarioActual: false,
  estudios: [{ idCarrera: 1, carrera: 'Medicina', idUniversidad: 1, universidad: 'Universidad Uno', siglaUniversidad: 'UU', semestre: 2 }]
};

export const OTRO_USUARIO_EJEMPLO: UsuarioPublicoModel = {
  ...USUARIO_EJEMPLO, id: 2, nombre: 'Luis', nombreCompleto: 'Luis Prueba', intereses: [], meGustaDelUsuarioActual: true,
  estudios: [{ idCarrera: 2, carrera: 'Derecho', idUniversidad: 1, universidad: 'Universidad Uno', siglaUniversidad: 'UU', semestre: 8 }]
};
