// Refleja LoginRequestDTO. El campo "correo" también acepta un usuario simple (ej. un administrador).
export interface LoginRequestModel {
  correo: string;
  contrasena: string;
}
