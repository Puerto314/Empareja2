export interface ErrorResponseModel {
  timestamp: string; status: number; error: string; message: string; path: string; details: string[] | null;
  /** Formato por defecto de Spring para errores de validación (por si el back no usa details). */
  errors?: { defaultMessage?: string; field?: string }[] | null;
}
