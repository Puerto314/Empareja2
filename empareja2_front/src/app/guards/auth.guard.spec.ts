import { TestBed } from '@angular/core/testing';
import { Router } from '@angular/router';

import { authGuard } from './auth.guard';

describe('authGuard', () => {
  let rutaDestino: string[] = [];

  beforeEach(() => {
    rutaDestino = [];
    localStorage.clear();
    TestBed.configureTestingModule({
      providers: [{ provide: Router, useValue: { navigate: (ruta: string[]) => { rutaDestino = ruta; } } }]
    });
  });

  it('debe permitir el acceso si hay token', () => {
    localStorage.setItem('token', 'token-falso');
    const resultado = TestBed.runInInjectionContext(() => authGuard({} as any, {} as any));
    expect(resultado).toBe(true);
  });

  it('debe bloquear el acceso y enviar al login si no hay token', () => {
    const resultado = TestBed.runInInjectionContext(() => authGuard({} as any, {} as any));
    expect(resultado).toBe(false);
    expect(rutaDestino).toEqual(['/login']);
  });
});
