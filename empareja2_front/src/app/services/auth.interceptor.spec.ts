import { TestBed } from '@angular/core/testing';
import { HTTP_INTERCEPTORS, HttpClient, provideHttpClient, withInterceptorsFromDi } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { Router } from '@angular/router';

import { AuthInterceptor } from './auth.interceptor';
<<<<<<< HEAD
import { AuthService } from './auth.service';
import { API_URL } from '../config/api.config';
=======
>>>>>>> main

describe('AuthInterceptor', () => {
  let http: HttpClient;
  let controller: HttpTestingController;
<<<<<<< HEAD
  let auth: AuthService;
  let rutaDestino: string[] = [];

  beforeEach(() => {
    rutaDestino = [];
    localStorage.clear();
=======
  let rutaDestino: string[] = [];

  beforeEach(() => {
    localStorage.clear();
    rutaDestino = [];
>>>>>>> main
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
<<<<<<< HEAD
    auth = TestBed.inject(AuthService);
=======
>>>>>>> main
  });

  afterEach(() => controller.verify());

<<<<<<< HEAD
  it('debe enviar el token Bearer al backend', () => {
    auth.guardarToken('jwt-123');
    http.get(`${API_URL}/usuarios`).subscribe();
    const req = controller.expectOne(`${API_URL}/usuarios`);
    expect(req.request.headers.get('Authorization')).toBe('Bearer jwt-123');
    req.flush([]);
  });

  it('no debe enviar el token a otros dominios', () => {
    auth.guardarToken('jwt-123');
    http.get('https://otro-sitio.com/datos').subscribe();
    const req = controller.expectOne('https://otro-sitio.com/datos');
=======
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
>>>>>>> main
    expect(req.request.headers.has('Authorization')).toBe(false);
    req.flush({});
  });

<<<<<<< HEAD
  it('no debe poner el header si no hay token', () => {
    http.get(`${API_URL}/catalogos/universidades`).subscribe();
    const req = controller.expectOne(`${API_URL}/catalogos/universidades`);
    expect(req.request.headers.has('Authorization')).toBe(false);
    req.flush([]);
  });

  it('debe cerrar sesión y volver al login ante un 401', () => {
    auth.guardarToken('vencido');
    http.get(`${API_URL}/usuarios`).subscribe({ error: () => {} });
    controller.expectOne(`${API_URL}/usuarios`).flush(null, { status: 401, statusText: 'Unauthorized' });
    expect(auth.estaLogueado()).toBe(false);
    expect(rutaDestino).toEqual(['/login']);
  });

  it('no debe redirigir ante un 401 del propio login (credenciales malas)', () => {
    http.post(`${API_URL}/auth/login`, {}).subscribe({ error: () => {} });
    controller.expectOne(`${API_URL}/auth/login`).flush(null, { status: 401, statusText: 'Unauthorized' });
    expect(rutaDestino).toEqual([]);
  });
=======
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
>>>>>>> main
});
