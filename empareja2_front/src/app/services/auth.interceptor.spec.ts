import { TestBed } from '@angular/core/testing';
import { HTTP_INTERCEPTORS, HttpClient, provideHttpClient, withInterceptorsFromDi } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { Router } from '@angular/router';

import { AuthInterceptor } from './auth.interceptor';

describe('AuthInterceptor', () => {
  let http: HttpClient;
  let controller: HttpTestingController;
  let rutaDestino: string[] = [];

  beforeEach(() => {
    localStorage.clear();
    rutaDestino = [];
    TestBed.configureTestingModule({
      providers: [
        provideHttpClient(withInterceptorsFromDi()),
        provideHttpClientTesting(),
        { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true },
        { provide: Router, useValue: { navigate: (ruta: string[]) => { rutaDestino = ruta; } } }
      ]
    });
    http = TestBed.inject(HttpClient);
    controller = TestBed.inject(HttpTestingController);
  });

  afterEach(() => controller.verify());

  it('debe añadir el token a las peticiones', () => {
    localStorage.setItem('token', 'abc');
    http.get('/prueba').subscribe();
    const req = controller.expectOne('/prueba');
    expect(req.request.headers.get('Authorization')).toBe('Bearer abc');
    req.flush({});
  });

  it('no debe añadir encabezado si no hay token', () => {
    http.get('/prueba').subscribe();
    const req = controller.expectOne('/prueba');
    expect(req.request.headers.has('Authorization')).toBe(false);
    req.flush({});
  });

  /* PRUEBA C: descomenta estos tests cuando actives el cierre de sesión por 401
  it('debe cerrar la sesión y volver al login si el token es rechazado', () => {
    localStorage.setItem('token', 'abc');
    http.get('/prueba').subscribe({ error: () => {} });
    controller.expectOne('/prueba').flush({}, { status: 401, statusText: 'Unauthorized' });
    expect(localStorage.getItem('token')).toBeNull();
    expect(rutaDestino).toEqual(['/login']);
  });

  it('no debe redirigir un 401 cuando no había sesión (login con credenciales malas)', () => {
    http.get('/prueba').subscribe({ error: () => {} });
    controller.expectOne('/prueba').flush({}, { status: 401, statusText: 'Unauthorized' });
    expect(rutaDestino).toEqual([]);
  });
  fin PRUEBA C */
});
