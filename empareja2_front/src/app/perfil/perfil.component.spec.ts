import { ComponentFixture, TestBed } from '@angular/core/testing';
import { NO_ERRORS_SCHEMA } from '@angular/core';
import { ActivatedRoute, convertToParamMap } from '@angular/router';
import { of, throwError } from 'rxjs';

import { PerfilComponent } from './perfil.component';
import { UsuarioService } from '../services/usuario.service';
import { OTRO_USUARIO_EJEMPLO, USUARIO_EJEMPLO } from '../testing/fixtures';

const estudio = { idCarrera: 1, carrera: 'Medicina', idUniversidad: 5, universidad: 'Universidad Uno', siglaUniversidad: 'UU', semestre: 2 };
const ana: any = {
  id: 1, nombre: 'Ana', apellido: 'Prueba', nombreCompleto: 'Ana Prueba', edad: 21, genero: 'FEMENINO', fotoPerfil: null,
  intereses: ['Cine', 'Rock'], redes: [{ idRedSocial: 1, redSocial: 'Instagram', urlPerfil: 'https://instagram.com/ana', alias: '@ana' }],
  estudios: [estudio], totalMeGusta: 3, meGustaDelUsuarioActual: false
};
const luis: any = { ...ana, id: 2, nombre: 'Luis', nombreCompleto: 'Luis Prueba', intereses: [], redes: [] };

describe('PerfilComponent', () => {
  let fixture: ComponentFixture<PerfilComponent>;
  let component: PerfilComponent;
  const serviceMock: any = {};

<<<<<<< HEAD
  const serviceMock: any = {};
  beforeEach(() => {
    serviceMock.obtenerPorId = () => of(ana);
    serviceMock.listarCompaneros = () => of([luis]);
  });

=======
>>>>>>> main
  const crear = async () => {
    await TestBed.configureTestingModule({
      declarations: [PerfilComponent],
      providers: [
<<<<<<< HEAD
        { provide: EstudianteService, useValue: serviceMock },
=======
        { provide: UsuarioService, useValue: serviceMock },
>>>>>>> main
        { provide: ActivatedRoute, useValue: { paramMap: of(convertToParamMap({ id: '1' })) } }
      ],
      schemas: [NO_ERRORS_SCHEMA]
    }).compileComponents();
    fixture = TestBed.createComponent(PerfilComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  };
<<<<<<< HEAD

  const texto = () => fixture.nativeElement.textContent as string;

  it('debe crearse', async () => {
    await crear();
    expect(fixture.componentInstance).toBeTruthy();
  });

  it('debe mostrar el nombre del estudiante', async () => {
    await crear();
    expect(fixture.nativeElement.querySelector('h1').textContent).toContain('Ana Prueba');
  });

  it('debe mostrar gustos, edad, género y datos académicos', async () => {
    await crear();
    expect(texto()).toContain('Cine');
    expect(texto()).toContain('21 años');
    expect(texto()).toContain('Femenino');
    expect(texto()).toContain('Medicina');
  });

  it('debe mostrar las redes sociales como enlaces externos seguros', async () => {
    await crear();
    const enlace = fixture.nativeElement.querySelector('a.red') as HTMLAnchorElement;
    expect(enlace.href).toBe('https://instagram.com/ana');
    expect(enlace.rel).toContain('noopener');
    expect(enlace.target).toBe('_blank');
  });

  it('debe listar compañeros de la misma universidad', async () => {
    await crear();
    expect(fixture.nativeElement.querySelectorAll('.mini-row:not(.red)').length).toBe(1);
  });

  it('debe avisar si el perfil no existe', async () => {
    serviceMock.obtenerPorId = () => of(undefined);
    await crear();
    expect(texto()).toContain('No encontramos este perfil');
  });

  it('debe mostrar un mensaje si el servidor falla', async () => {
    serviceMock.obtenerPorId = () => throwError(() => new Error('falló'));
    await crear();
    expect(texto()).toContain('No pudimos cargar este perfil');
=======

  beforeEach(() => {
    serviceMock.obtenerPublico = () => of(USUARIO_EJEMPLO);
    serviceMock.listarCatalogo = () => of([USUARIO_EJEMPLO, OTRO_USUARIO_EJEMPLO]);
    serviceMock.darMeGusta = () => of({ idUsuario: 1, totalMeGusta: 4, meGusta: true });
    serviceMock.quitarMeGusta = () => of({ idUsuario: 1, totalMeGusta: 2, meGusta: false });
  });

  it('debe crearse y mostrar el nombre', async () => {
    await crear();
    expect(component).toBeTruthy();
    expect(fixture.nativeElement.querySelector('h1').textContent).toContain('Ana Prueba');
  });

  /* PRUEBAS A y B: descomenta estos tests cuando actives el me gusta y los compañeros
  it('debe listar compañeros sin incluir al propio perfil', async () => {
    await crear();
    expect(component.otros().map(o => o.id)).toEqual([2]);
    expect(fixture.nativeElement.querySelectorAll('.mini-row').length).toBe(1);
>>>>>>> main
  });

  it('debe dar me gusta y actualizar el contador', async () => {
    await crear();
    component.alternarMeGusta();
    expect(component.u()?.totalMeGusta).toBe(4);
    expect(component.u()?.meGustaDelUsuarioActual).toBe(true);
  });

  it('debe quitar el me gusta si ya estaba dado', async () => {
    serviceMock.obtenerPublico = () => of({ ...USUARIO_EJEMPLO, totalMeGusta: 3, meGustaDelUsuarioActual: true });
    await crear();
    component.alternarMeGusta();
    expect(component.u()?.totalMeGusta).toBe(2);
    expect(component.u()?.meGustaDelUsuarioActual).toBe(false);
  });

  fin PRUEBAS A y B */

  it('debe mostrar el error si el perfil no existe', async () => {
    serviceMock.obtenerPublico = () => throwError(() => ({ status: 404, error: { message: 'No existe el usuario' } }));
    await crear();
    expect(component.error()).toBe('No existe el usuario');
  });
});
