import { HttpErrorResponse } from '@angular/common/http';
import { ErrorResponseModel } from '../models/error-response.model';

function detallesDe(cuerpo: Partial<ErrorResponseModel>): string[] {
  if (Array.isArray(cuerpo.details) && cuerpo.details.length) {
    return cuerpo.details.filter(d => typeof d === 'string' && d.trim() !== '');
  }
  if (Array.isArray(cuerpo.errors) && cuerpo.errors.length) {
    return cuerpo.errors
      .map(e => (e && typeof e.defaultMessage === 'string' ? e.defaultMessage : ''))
      .filter(d => d.trim() !== '');
  }
  return [];
}

/**
 * Convierte cualquier error en el texto que se muestra al usuario. Siempre que el
 * back haya enviado un mensaje (excepción de negocio, validación, 401, 403, 500…),
 * ese mensaje es el que se muestra; el texto por defecto solo se usa si el back no
 * respondió o respondió sin cuerpo.
 */
export function mensajeDeError(err: unknown, porDefecto = 'Ocurrió un error. Intenta de nuevo.'): string {
  if (!(err instanceof HttpErrorResponse)) { return porDefecto; }
  if (err.status === 0) { return 'No se pudo conectar con el servidor. Intenta de nuevo en un momento.'; }
  const cuerpo = err.error as Partial<ErrorResponseModel> | null;
  if (cuerpo && typeof cuerpo === 'object') {
    const detalles = detallesDe(cuerpo);
    const sufijo = detalles.length ? ` (${detalles.join('; ')})` : '';
    if (typeof cuerpo.message === 'string' && cuerpo.message.trim() !== '') { return cuerpo.message + sufijo; }
    if (detalles.length) { return detalles.join('; '); }
  }
  if (err.status === 403) { return 'No tienes permiso para realizar esta acción.'; }
  return porDefecto;
}
