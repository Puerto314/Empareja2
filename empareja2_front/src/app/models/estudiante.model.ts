// Refleja StudentPublicDTO. Los campos opcionales aún no existen en el backend.
export interface EstudianteModel {
  id: number;
  fullName: string;
  career: string;
  semester: number;
  university: string;
  bio?: string;
  interests?: string[];
  photos?: string[];
}
