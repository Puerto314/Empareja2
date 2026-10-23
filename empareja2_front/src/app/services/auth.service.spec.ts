import { TestBed } from '@angular/core/testing';

import { AuthService } from './auth.service';

describe('AuthService', () => {
  let service: AuthService;

  beforeEach(() => {
    localStorage.clear();
    TestBed.configureTestingModule({});
    service = TestBed.inject(AuthService);
  });

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
});
