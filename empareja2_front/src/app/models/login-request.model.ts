<<<<<<< HEAD
// Refleja LoginRequestDTO. El campo "correo" también acepta un usuario simple (ej. un administrador).
=======
// Refleja LoginRequestDTO ("correo" también acepta el usuario del administrador).
>>>>>>> main
export interface LoginRequestModel {
  correo: string;
  contrasena: string;
}
