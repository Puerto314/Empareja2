// Refleja ErrorResponseDTO: cuerpo de los errores del backend.
export interface ErrorResponseModel {
  timestamp: string; status: number; error: string; message: string; path: string; details: string[] | null;
}
