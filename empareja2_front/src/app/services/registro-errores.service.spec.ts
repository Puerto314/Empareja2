import { TestBed } from '@angular/core/testing';
import { HttpErrorResponse } from '@angular/common/http';

import { RegistroErroresService } from './registro-errores.service';
import { ManejadorGlobalErrores } from './manejador-global-errores';
import { API_URL } from '../config/api.config';

describe('RegistroErroresService', () => {
  let servicio: RegistroErroresService;
  let aviso: jasmine.Spy;
  let fallo: jasmine.Spy;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    servicio = TestBed.inject(RegistroErroresService);
    aviso = spyOn(console, 'warn');
    fallo = spyOn(console, 'error');
  });

  it('debe registrar un error 4xx como aviso con método, ruta y mensaje', () => {
    const err = new HttpErrorResponse({
      status: 409, url: `${API_URL}/admin/ciudades`,
      error: { message: 'Ya existe esa ciudad en ese departamento' }
    });
    servicio.http(err, 'POST');
    expect(aviso).toHaveBeenCalledTimes(1);
    expect(aviso.calls.mostRecent().args[0]).toBe('[empareja2] HTTP 409 POST /admin/ciudades -> Ya existe esa ciudad en ese departamento');
    expect(fallo).not.toHaveBeenCalled();
  });

  it('debe registrar un error 5xx como error', () => {
    servicio.http(new HttpErrorResponse({ status: 500, url: `${API_URL}/usuarios`, error: { message: 'Fallo interno' } }), 'GET');
    expect(fallo).toHaveBeenCalledTimes(1);
    expect(aviso).not.toHaveBeenCalled();
  });

  it('debe registrar como error la falta de conexión', () => {
    servicio.http(new HttpErrorResponse({ status: 0, url: `${API_URL}/usuarios` }), 'GET');
    expect(fallo.calls.mostRecent().args[0]).toContain('HTTP sin respuesta GET /usuarios');
  });

  it('no debe registrar dos veces el mismo error HTTP', () => {
    const err = new HttpErrorResponse({ status: 404, url: `${API_URL}/usuarios/9` });
    servicio.http(err, 'GET');
    servicio.http(err, 'GET');
    expect(aviso).toHaveBeenCalledTimes(1);
  });

  it('debe registrar una excepción de código con su mensaje y su traza', () => {
    servicio.excepcion(new TypeError('x es undefined'));
    expect(fallo).toHaveBeenCalledTimes(1);
    expect(fallo.calls.mostRecent().args[0]).toBe('[empareja2] Excepción no controlada: TypeError: x es undefined');
  });

  it('debe describir valores que no son Error', () => {
    servicio.excepcion('algo falló');
    servicio.excepcion({ codigo: 7 });
    expect(fallo.calls.argsFor(0)[0]).toBe('[empareja2] Excepción no controlada: algo falló');
    expect(fallo.calls.argsFor(1)[0]).toBe('[empareja2] Excepción no controlada: {"codigo":7}');
  });

  it('debe tolerar valores con referencias circulares', () => {
    const circular: Record<string, unknown> = {};
    circular['yo'] = circular;
    expect(() => servicio.excepcion(circular)).not.toThrow();
    expect(fallo).toHaveBeenCalledTimes(1);
  });
});

describe('ManejadorGlobalErrores', () => {
  let manejador: ManejadorGlobalErrores;
  let fallo: jasmine.Spy;

  beforeEach(() => {
    TestBed.configureTestingModule({ providers: [ManejadorGlobalErrores] });
    manejador = TestBed.inject(ManejadorGlobalErrores);
    fallo = spyOn(console, 'error');
    spyOn(console, 'warn');
  });

  it('debe registrar las excepciones no controladas', () => {
    manejador.handleError(new RangeError('fuera de rango'));
    expect(fallo.calls.mostRecent().args[0]).toContain('RangeError: fuera de rango');
  });

  it('debe abrir el rechazo de una promesa antes de registrarlo', () => {
    manejador.handleError({ rejection: new Error('promesa rechazada') });
    expect(fallo.calls.mostRecent().args[0]).toContain('Error: promesa rechazada');
  });
});
