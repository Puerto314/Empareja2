import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';

import { AuthService } from './auth.service';
import { API_URL } from '../config/api.config';
import { TipoDocumentoEnum } from '../models/tipo-documento.enum';

describe('AuthService', () => {
  let service: AuthService;
  let http: HttpTestingController;

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

  it('debe guardar y leer el token', () => {
    service.guardarToken('abc');
    expect(service.obtenerToken()).toBe('abc');
    expect(service.estaLogueado()).toBe(true);
  });

  it('debe borrar el token al cerrar sesión', () => {
    service.guardarToken('abc');
    service.cerrarSesion();
    expect(service.obtenerToken()).toBeNull();
    expect(service.estaLogueado()).toBe(false);
  });

  it('debe guardar la sesión (token + usuario) y saber si es administrador', () => {
    service.guardarSesion({ token: 'jwt', tokenType: 'Bearer', expiresInMs: 1, usuario: { id: 7, rol: 'ADMIN' } as any });
    expect(service.obtenerToken()).toBe('jwt');
    expect(service.obtenerUsuario()?.id).toBe(7);
    expect(service.esAdmin()).toBe(true);
    service.cerrarSesion();
    expect(service.obtenerUsuario()).toBeNull();
    expect(service.esAdmin()).toBe(false);
  });

  it('debe tolerar un usuario guardado dañado', () => {
    localStorage.setItem('usuario', '{no es json');
    expect(service.obtenerUsuario()).toBeNull();
  });

  it('debe iniciar sesión con POST /auth/login usando correo y contrasena', () => {
    let token = '';
    service.iniciarSesion('ana@unbosque.edu.co', '12345678').subscribe(r => token = r.token);

    const req = http.expectOne(`${API_URL}/auth/login`);
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual({ correo: 'ana@unbosque.edu.co', contrasena: '12345678' });
    req.flush({ token: 'jwt-123', tokenType: 'Bearer', expiresInMs: 86400000, usuario: {} });

    expect(token).toBe('jwt-123');
  });

  it('debe registrar con POST /auth/registro', () => {
    service.registrar({
      correo: 'ana@unbosque.edu.co', contrasena: '12345678', tipoDocumento: TipoDocumentoEnum.CC, documento: '1',
      nombre: 'Ana', apellido: 'Prueba', fechaNacimiento: '2002-05-01',
      estudios: [{ idCarrera: 1, idUniversidad: 1, semestre: 3 }]
    }).subscribe();

    const req = http.expectOne(`${API_URL}/auth/registro`);
    expect(req.request.method).toBe('POST');
    expect(req.request.body.estudios.length).toBe(1);
    req.flush({});
  });
});
