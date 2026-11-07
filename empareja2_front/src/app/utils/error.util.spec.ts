import { HttpErrorResponse } from '@angular/common/http';
import { mensajeDeError } from './error.util';

describe('mensajeDeError', () => {
  it('devuelve el texto por defecto si no es un error HTTP', () => {
    expect(mensajeDeError(new Error('x'), 'Por defecto')).toBe('Por defecto');
  });

  it('avisa cuando no hay conexión', () => {
    expect(mensajeDeError(new HttpErrorResponse({ status: 0 }))).toContain('No se pudo conectar');
  });

  it('muestra el mensaje de la excepción del back', () => {
    const err = new HttpErrorResponse({ status: 400, error: { message: 'Debes indicar exactamente una carrera y universidad' } });
    expect(mensajeDeError(err, 'Por defecto')).toBe('Debes indicar exactamente una carrera y universidad');
  });

  it('agrega los detalles de validación (campos faltantes)', () => {
    const err = new HttpErrorResponse({
      status: 400, error: { message: 'Hay datos inválidos', details: ['El nombre es obligatorio', 'El apellido es obligatorio'] }
    });
    expect(mensajeDeError(err)).toBe('Hay datos inválidos (El nombre es obligatorio; El apellido es obligatorio)');
  });

  it('entiende el formato por defecto de Spring (errors[].defaultMessage)', () => {
    const err = new HttpErrorResponse({ status: 400, error: { errors: [{ defaultMessage: 'El nombre es obligatorio' }] } });
    expect(mensajeDeError(err)).toBe('El nombre es obligatorio');
  });

  it('en 403 usa el mensaje del back y, si no hay, uno fijo', () => {
    expect(mensajeDeError(new HttpErrorResponse({ status: 403, error: { message: 'Acceso denegado' } }))).toBe('Acceso denegado');
    expect(mensajeDeError(new HttpErrorResponse({ status: 403 }))).toContain('No tienes permiso');
  });

  it('en 401 o 500 sin cuerpo usa el texto por defecto', () => {
    expect(mensajeDeError(new HttpErrorResponse({ status: 401 }), 'Credenciales incorrectas')).toBe('Credenciales incorrectas');
    expect(mensajeDeError(new HttpErrorResponse({ status: 500, error: '<html>' }), 'Fallo')).toBe('Fallo');
  });
});
