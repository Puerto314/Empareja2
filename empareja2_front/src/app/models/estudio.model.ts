// Refleja EstudioDTO: una carrera que cursa el usuario en una universidad.
export interface EstudioModel {
  idCarrera: number;
  carrera: string;
  idUniversidad: number;
  universidad: string;
  siglaUniversidad: string;
  semestre: number;
}
