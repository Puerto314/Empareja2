import { ComponentFixture, TestBed } from '@angular/core/testing';
import { NO_ERRORS_SCHEMA } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { of, throwError } from 'rxjs';

import { LoginComponent } from './login.component';
import { AuthService } from '../services/auth.service';

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let rutaDestino: string[] = [];
  let datosEnviados: any;

  const authServiceMock: any = {};
  const routerMock = { navigate: (ruta: string[]) => { rutaDestino = ruta; } };

  beforeEach(async () => {
    rutaDestino = [];
    datosEnviados = undefined;
    authServiceMock.iniciarSesion = (datos: any) => { datosEnviados = datos; return of({}); };

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
  });
});
