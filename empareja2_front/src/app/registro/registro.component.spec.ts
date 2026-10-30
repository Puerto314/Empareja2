import { TestBed } from '@angular/core/testing';
import { NO_ERRORS_SCHEMA } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { Router } from '@angular/router';
import { of, throwError } from 'rxjs';

import { RegistroComponent } from './registro.component';
import { AuthService } from '../services/auth.service';

describe('RegistroComponent', () => {
  let ruta: string[] = [];
  let sesion: any = null;
  const auth: any = {};

  beforeEach(async () => {
    ruta = []; sesion = null;
    auth.registrar = () => of({ token: 'jwt', tokenType: 'Bearer', expiresInMs: 1, usuario: { id: 1, rol: 'ESTUDIANTE' } });
    auth.guardarSesion = (r: unknown) => { sesion = r; };
    await TestBed.configureTestingModule({
      declarations: [RegistroComponent],
      providers: [{ provide: AuthService, useValue: auth }, { provide: Router, useValue: { navigate: (r: string[]) => { ruta = r; } } }],
      schemas: [NO_ERRORS_SCHEMA]
    }).compileComponents();
  });

  it('debe registrar, iniciar sesión con el token del back y navegar al catálogo', () => {
    const comp = TestBed.createComponent(RegistroComponent).componentInstance;
    comp.registrar({ correo: 'a@b.co' } as any);
    expect(sesion.token).toBe('jwt');
    expect(ruta).toEqual(['/universidades']);
    expect(comp.cargando()).toBe(false);
  });

  it('debe mostrar el mensaje y los detalles de validación del back', () => {
    auth.registrar = () => throwError(() => new HttpErrorResponse({
      status: 400, error: { message: 'Error de validacion en los datos enviados', details: ['correo: El correo es obligatorio'] }
    }));
    const comp = TestBed.createComponent(RegistroComponent).componentInstance;
    comp.registrar({} as any);
    expect(comp.error()).toContain('Error de validacion');
    expect(comp.error()).toContain('El correo es obligatorio');
    expect(ruta).toEqual([]);
  });

  it('debe mostrar el 409 de correo duplicado', () => {
    auth.registrar = () => throwError(() => new HttpErrorResponse({ status: 409, error: { message: 'Ya existe un usuario registrado con ese correo' } }));
    const comp = TestBed.createComponent(RegistroComponent).componentInstance;
    comp.registrar({} as any);
    expect(comp.error()).toBe('Ya existe un usuario registrado con ese correo');
  });

  it('debe avisar si no hay conexión', () => {
    auth.registrar = () => throwError(() => new HttpErrorResponse({ status: 0 }));
    const comp = TestBed.createComponent(RegistroComponent).componentInstance;
    comp.registrar({} as any);
    expect(comp.error()).toContain('No se pudo conectar');
  });
});
