// Refleja RegisterRequestDTO.
export interface RegistroRequestModel {
  email: string;
  password: string;
  fullName: string;
  career: string;
  semester: number;
  university: string;
  address?: string;
  birthDate?: string;
}
