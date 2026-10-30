import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { Router } from '@angular/router';

import { adminGuard } from './admin.guard';

describe('adminGuard', () => {
  let rutaDestino: string[] = [];

  beforeEach(() => {
    rutaDestino = [];
    localStorage.clear();
    TestBed.configureTestingModule({
      providers: [
        provideHttpClient(), provideHttpClientTesting(),
        { provide: Router, useValue: { navigate: (r: string[]) => { rutaDestino = r; } } }
      ]
    });
  });

  const correr = () => TestBed.runInInjectionContext(() => adminGuard({} as any, {} as any));

  it('debe dejar pasar a un administrador', () => {
    localStorage.setItem('token', 't');
    localStorage.setItem('usuario', JSON.stringify({ rol: 'ADMIN' }));
    expect(correr()).toBe(true);
  });

  it('debe enviar a un estudiante al catálogo', () => {
    localStorage.setItem('token', 't');
    localStorage.setItem('usuario', JSON.stringify({ rol: 'ESTUDIANTE' }));
    expect(correr()).toBe(false);
    expect(rutaDestino).toEqual(['/universidades']);
  });

  it('debe enviar al login si no hay sesión', () => {
    expect(correr()).toBe(false);
    expect(rutaDestino).toEqual(['/login']);
  });
});
