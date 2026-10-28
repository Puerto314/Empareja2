import { ComponentFixture, TestBed } from '@angular/core/testing';
import { NO_ERRORS_SCHEMA } from '@angular/core';
import { of, throwError } from 'rxjs';

import { UniversidadesComponent } from './universidades.component';
<<<<<<< HEAD
import { UniversidadService } from '../services/universidad.service';

const base = { calle: null, barrio: null, numeroDireccion: null, ciudad: null };

describe('UniversidadesComponent', () => {
  const serviceMock: any = {};
  beforeEach(() => {
    serviceMock.listarResumen = () => of({
      totalEstudiantes: 3,
      universidades: [
        { idUniversidad: 1, nombre: 'Universidad Uno', sigla: 'UU', ...base, cantidad: 2, vistaPrevia: [] },
        { idUniversidad: 2, nombre: 'Universidad Dos', sigla: 'UD', ...base, cantidad: 1, vistaPrevia: [] }
      ]
    });
  });

  const crear = async (): Promise<ComponentFixture<UniversidadesComponent>> => {
    await TestBed.configureTestingModule({
      declarations: [UniversidadesComponent],
      providers: [{ provide: UniversidadService, useValue: serviceMock }],
      schemas: [NO_ERRORS_SCHEMA]
    }).compileComponents();
    const fixture = TestBed.createComponent(UniversidadesComponent);
=======
import { CatalogoService } from '../services/catalogo.service';
import { UsuarioService } from '../services/usuario.service';
import { OTRO_USUARIO_EJEMPLO, UNIVERSIDAD_EJEMPLO, UNIVERSIDAD_VACIA_EJEMPLO, USUARIO_EJEMPLO } from '../testing/fixtures';

describe('UniversidadesComponent', () => {
  const crear = async (usuarios$: any = of([USUARIO_EJEMPLO, OTRO_USUARIO_EJEMPLO])) => {
    await TestBed.configureTestingModule({
      declarations: [UniversidadesComponent],
      providers: [
        { provide: CatalogoService, useValue: { universidades: () => of([UNIVERSIDAD_VACIA_EJEMPLO, UNIVERSIDAD_EJEMPLO]) } },
        { provide: UsuarioService, useValue: { listarCatalogo: () => usuarios$ } }
      ],
      schemas: [NO_ERRORS_SCHEMA]
    }).compileComponents();
    const fixture: ComponentFixture<UniversidadesComponent> = TestBed.createComponent(UniversidadesComponent);
>>>>>>> main
    fixture.detectChanges();
    return fixture;
  };

  it('debe crearse', async () => {
    expect((await crear()).componentInstance).toBeTruthy();
  });

<<<<<<< HEAD
  it('debe mostrar el total de estudiantes que entrega el servicio', async () => {
    expect((await crear()).componentInstance.total()).toBe(3);
=======
  it('debe contar estudiantes por universidad y poner primero la que más tiene', async () => {
    const c = (await crear()).componentInstance;
    expect(c.total()).toBe(2);
    expect(c.unis().map(r => r.universidad.nombre)).toEqual(['Universidad Uno', 'Universidad Dos']);
    expect(c.unis().map(r => r.cantidad)).toEqual([2, 0]);
>>>>>>> main
  });

  it('debe mostrar una tarjeta por universidad', async () => {
    const fixture = await crear();
    expect(fixture.nativeElement.querySelectorAll('.tile').length).toBe(2);
  });

<<<<<<< HEAD
  it('debe mostrar un mensaje si el servidor falla', async () => {
    serviceMock.listarResumen = () => throwError(() => new Error('falló'));
    const fixture = await crear();
    expect(fixture.nativeElement.querySelector('.error').textContent).toContain('No pudimos cargar las universidades');
    expect(fixture.nativeElement.querySelectorAll('.tile').length).toBe(0);
=======
  it('debe mostrar el error si el backend falla', async () => {
    const fixture = await crear(throwError(() => ({ status: 500, error: { message: 'Fallo del servidor' } })));
    expect(fixture.componentInstance.error()).toBe('Fallo del servidor');
    expect(fixture.componentInstance.cargando()).toBe(false);
>>>>>>> main
  });
});
