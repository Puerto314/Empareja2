import { ComponentFixture, TestBed } from '@angular/core/testing';
import { NO_ERRORS_SCHEMA } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { of, throwError } from 'rxjs';

import { LoginComponent } from './login.component';
import { AuthService } from '../services/auth.service';

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let rutaDestino: string[] = [];
<<<<<<< HEAD
  let tokenGuardado = '';
  let credenciales: string[] = [];

  const authServiceMock: any = {
    guardarToken: (token: string) => { tokenGuardado = token; }
  };
=======
  let datosEnviados: any;

  const authServiceMock: any = {};
>>>>>>> main
  const routerMock = { navigate: (ruta: string[]) => { rutaDestino = ruta; } };

  beforeEach(async () => {
    rutaDestino = [];
<<<<<<< HEAD
    tokenGuardado = '';
    credenciales = [];
    authServiceMock.iniciarSesion = (correo: string, contrasena: string) => {
      credenciales = [correo, contrasena];
      return of({ token: 'token-falso', tokenType: 'Bearer', expiresInMs: 1, usuario: {} });
    };
=======
    datosEnviados = undefined;
    authServiceMock.iniciarSesion = (datos: any) => { datosEnviados = datos; return of({}); };
>>>>>>> main

    await TestBed.configureTestingModule({
      declarations: [LoginComponent],
      imports: [FormsModule],
      providers: [
        { provide: AuthService, useValue: authServiceMock },
        { provide: Router, useValue: routerMock }
      ],
      schemas: [NO_ERRORS_SCHEMA]
    }).compileComponents();

    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('debe crearse', () => {
    expect(component).toBeTruthy();
  });

  it('debe pedir los datos si el formulario está vacío', () => {
    component.entrar();
    expect(component.error()).toBe('Escribe tu correo y tu contraseña para entrar.');
    expect(datosEnviados).toBeUndefined();
  });

<<<<<<< HEAD
  it('debe iniciar sesión, guardar el token del backend y navegar a universidades', () => {
    component.email = '  ana@unbosque.edu.co ';
    component.password = '12345678';
    component.entrar();
    expect(credenciales).toEqual(['ana@unbosque.edu.co', '12345678']);
    expect(tokenGuardado).toBe('token-falso');
    expect(rutaDestino).toEqual(['/universidades']);
  });

  it('debe aceptar un usuario sin arroba (ej. administrador)', () => {
    component.email = 'Puerto314';
    component.password = 'secreta';
    component.entrar();
    expect(credenciales[0]).toBe('Puerto314');
    expect(component.error()).toBe('');
  });

  it('debe enviar el formulario con Enter (submit)', () => {
    component.email = 'ana@unbosque.edu.co';
    component.password = '12345678';
    fixture.nativeElement.querySelector('form').dispatchEvent(new Event('submit'));
    expect(tokenGuardado).toBe('token-falso');
  });

  it('debe mostrar error si las credenciales son incorrectas (401)', () => {
    authServiceMock.iniciarSesion = () => throwError(() => new HttpErrorResponse({ status: 401 }));
    component.email = 'ana@unbosque.edu.co';
    component.password = 'mala-clave';
    component.entrar();
    expect(component.error()).toBe('Correo o contraseña incorrectos.');
    expect(component.cargando()).toBe(false);
  });

  it('debe avisar si no hay conexión con el servidor', () => {
    authServiceMock.iniciarSesion = () => throwError(() => new HttpErrorResponse({ status: 0 }));
    component.email = 'ana@unbosque.edu.co';
    component.password = '12345678';
    component.entrar();
    expect(component.error()).toContain('No se pudo conectar con el servidor');
=======
  it('debe enviar correo y contraseña y navegar a universidades', () => {
    component.correo = ' ana@unbosque.edu.co ';
    component.contrasena = '12345678';
    component.entrar();
    expect(datosEnviados).toEqual({ correo: 'ana@unbosque.edu.co', contrasena: '12345678' });
    expect(rutaDestino).toEqual(['/universidades']);
  });

  it('debe mostrar error si las credenciales son incorrectas', () => {
    authServiceMock.iniciarSesion = () => throwError(() => ({ status: 401 }));
    component.correo = 'ana@unbosque.edu.co';
    component.contrasena = 'mala';
    component.entrar();
    expect(component.error()).toBe('Correo o contraseña incorrectos.');
    expect(component.cargando()).toBe(false);
  });

  it('debe avisar si no hay conexión con el servidor', () => {
    authServiceMock.iniciarSesion = () => throwError(() => ({ status: 0 }));
    component.correo = 'ana@unbosque.edu.co';
    component.contrasena = '12345678';
    component.entrar();
    expect(component.error()).toContain('servidor');
>>>>>>> main
  });
});
