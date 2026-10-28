import { RolEnum } from './rol.enum';

// Refleja StudentProfileDTO (perfil completo, solo del dueño o un admin).
export interface PerfilModel {
  id: number;
  email: string;
  role: RolEnum;
  fullName: string;
  career: string;
  semester: number;
  university: string;
  address?: string;
  birthDate?: string;
  createdAt?: string;
}
