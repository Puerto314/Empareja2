import { ComponentFixture, TestBed } from '@angular/core/testing';
import { NO_ERRORS_SCHEMA } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { of, throwError } from 'rxjs';

import { RegistroComponent } from './registro.component';
import { AuthService } from '../services/auth.service';
import { CatalogoService } from '../services/catalogo.service';
import { UNIVERSIDAD_EJEMPLO } from '../testing/fixtures';

describe('RegistroComponent', () => {
  let component: RegistroComponent;
  let fixture: ComponentFixture<RegistroComponent>;
  let rutaDestino: string[] = [];
  let datosEnviados: any;

  const authServiceMock: any = {};
  const catalogoMock = {
    universidades: () => of([UNIVERSIDAD_EJEMPLO]),
    carreras: () => of([{ idCarrera: 1, nombre: 'Medicina' }])
  };

  const llenarFormulario = () => {
    Object.assign(component.f, {
      correo: 'ana@unbosque.edu.co', contrasena: '12345678', nombre: 'Ana', apellido: 'Prueba',
      documento: '123', fechaNacimiento: '2004-05-10', idUniversidad: 1, idCarrera: 1, semestre: 3
    });
  };

  beforeEach(async () => {
    rutaDestino = [];
    datosEnviados = undefined;
    authServiceMock.registrar = (datos: any) => { datosEnviados = datos; return of({}); };

    await TestBed.configureTestingModule({
      declarations: [RegistroComponent],
      imports: [FormsModule],
      providers: [
        { provide: AuthService, useValue: authServiceMock },
        { provide: CatalogoService, useValue: catalogoMock },
        { provide: Router, useValue: { navigate: (ruta: string[]) => { rutaDestino = ruta; } } }
      ],
      schemas: [NO_ERRORS_SCHEMA]
    }).compileComponents();

    fixture = TestBed.createComponent(RegistroComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('debe crearse y cargar universidades y carreras', () => {
    expect(component).toBeTruthy();
    expect(component.universidades().length).toBe(1);
    expect(component.carreras().length).toBe(1);
  });

  it('debe exigir los campos obligatorios', () => {
    component.registrar();
    expect(component.error()).toBe('Completa todos los campos obligatorios.');
    expect(datosEnviados).toBeUndefined();
  });

  it('debe exigir una contraseña de al menos 8 caracteres', () => {
    llenarFormulario();
    component.f.contrasena = '123';
    component.registrar();
    expect(component.error()).toBe('La contraseña debe tener al menos 8 caracteres.');
  });

  it('debe enviar el registro con un estudio y los gustos sin repetir', () => {
    llenarFormulario();
    component.f.intereses = 'Café, Indie, café, Indie, ';
    component.registrar();
    expect(datosEnviados.estudios).toEqual([{ idCarrera: 1, idUniversidad: 1, semestre: 3 }]);
    expect(datosEnviados.intereses).toEqual(['Café', 'Indie', 'café']);
    expect(rutaDestino).toEqual(['/universidades']);
  });

  it('debe mostrar los detalles de validación que devuelve el backend', () => {
    authServiceMock.registrar = () => throwError(() => ({ status: 400, error: { details: ['correo: ya existe'] } }));
    llenarFormulario();
    component.registrar();
    expect(component.error()).toBe('correo: ya existe');
  });
});
