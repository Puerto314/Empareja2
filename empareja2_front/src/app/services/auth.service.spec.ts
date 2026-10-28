import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';

import { AuthService } from './auth.service';
import { API_URL } from '../config/api.config';

describe('AuthService', () => {
  let service: AuthService;
  let http: HttpTestingController;

  const respuesta: any = { token: 'jwt-falso', tokenType: 'Bearer', expiresInMs: 60000, usuario: { id: 1, nombre: 'Ana' } };

  beforeEach(() => {
    localStorage.clear();
    TestBed.configureTestingModule({ providers: [provideHttpClient(), provideHttpClientTesting()] });
    service = TestBed.inject(AuthService);
    http = TestBed.inject(HttpTestingController);
  });

  afterEach(() => http.verify());

  it('debe crearse', () => {
    expect(service).toBeTruthy();
  });

  it('debe iniciar sesión y guardar token y usuario', () => {
    service.iniciarSesion({ correo: 'ana@x.co', contrasena: '12345678' }).subscribe();
    const req = http.expectOne(`${API_URL}/auth/login`);
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual({ correo: 'ana@x.co', contrasena: '12345678' });
    req.flush(respuesta);

    expect(service.obtenerToken()).toBe('jwt-falso');
    expect(service.obtenerUsuario()?.nombre).toBe('Ana');
    expect(service.estaLogueado()).toBe(true);
  });

  it('debe registrar usando el endpoint de registro', () => {
    service.registrar({} as any).subscribe();
    const req = http.expectOne(`${API_URL}/auth/registro`);
    expect(req.request.method).toBe('POST');
    req.flush(respuesta);
    expect(service.estaLogueado()).toBe(true);
  });

  it('no debe considerar logueado un token vencido', () => {
    service.guardarSesion({ ...respuesta, expiresInMs: -1000 });
    expect(service.estaLogueado()).toBe(false);
  });

  it('debe borrar la sesión al cerrarla', () => {
    service.guardarSesion(respuesta);
    service.cerrarSesion();
    expect(service.obtenerToken()).toBeNull();
    expect(service.obtenerUsuario()).toBeNull();
    expect(service.estaLogueado()).toBe(false);
  });
});
