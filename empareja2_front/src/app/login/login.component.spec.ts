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
  let tokenGuardado = '';

  const authServiceMock: any = {
    iniciarSesion: () => of('token-falso'),
    guardarToken: (token: string) => { tokenGuardado = token; }
  };
  const routerMock = { navigate: (ruta: string[]) => { rutaDestino = ruta; } };

  beforeEach(async () => {
    rutaDestino = [];
    tokenGuardado = '';
    authServiceMock.iniciarSesion = () => of('token-falso');

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
  });

  it('debe iniciar sesión y navegar a universidades', () => {
    component.email = 'ana@unbosque.edu.co';
    component.password = '12345678';
    component.entrar();
    expect(tokenGuardado).toBe('token-falso');
    expect(rutaDestino).toEqual(['/universidades']);
  });

  it('debe mostrar error si el inicio de sesión falla', () => {
    authServiceMock.iniciarSesion = () => throwError(() => new Error());
    component.email = 'ana@unbosque.edu.co';
    component.password = '12345678';
    component.entrar();
    expect(component.error()).toBe('Correo o contraseña incorrectos.');
  });
});
