import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { Router } from '@angular/router';

import { authGuard } from './auth.guard';
import { AuthService } from '../services/auth.service';

describe('authGuard', () => {
  let rutaDestino: string[] = [];
  let logueado = false;

  beforeEach(() => {
    rutaDestino = [];
    TestBed.configureTestingModule({
      providers: [
<<<<<<< HEAD
        provideHttpClient(),
        provideHttpClientTesting(),
=======
        { provide: AuthService, useValue: { estaLogueado: () => logueado } },
>>>>>>> main
        { provide: Router, useValue: { navigate: (ruta: string[]) => { rutaDestino = ruta; } } }
      ]
    });
  });

  it('debe permitir el acceso si hay sesión', () => {
    logueado = true;
    expect(TestBed.runInInjectionContext(() => authGuard({} as any, {} as any))).toBe(true);
  });

  it('debe bloquear el acceso y enviar al login si no hay sesión', () => {
    logueado = false;
    expect(TestBed.runInInjectionContext(() => authGuard({} as any, {} as any))).toBe(false);
    expect(rutaDestino).toEqual(['/login']);
  });
});
