import { HttpErrorResponse } from '@angular/common/http';
import { ErrorResponseModel } from '../models/error-response.model';

// Convierte un error HTTP en un mensaje para el usuario, usando el cuerpo ErrorResponseDTO del backend
// (message + details de validación) cuando existe.
export function mensajeDeError(err: unknown, porDefecto = 'Ocurrió un error. Intenta de nuevo.'): string {
  if (!(err instanceof HttpErrorResponse)) { return porDefecto; }
  if (err.status === 0) { return 'No se pudo conectar con el servidor. Intenta de nuevo en un momento.'; }
  if (err.status === 403) { return 'No tienes permiso para realizar esta acción.'; }
  const cuerpo = err.error as Partial<ErrorResponseModel> | null;
  if (cuerpo && typeof cuerpo === 'object') {
    const detalles = Array.isArray(cuerpo.details) && cuerpo.details.length ? ` (${cuerpo.details.join('; ')})` : '';
    if (cuerpo.message) { return cuerpo.message + detalles; }
  }
  return porDefecto;
}
