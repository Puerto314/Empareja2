import { ComponentFixture, TestBed } from '@angular/core/testing';
import { NO_ERRORS_SCHEMA } from '@angular/core';
import { ActivatedRoute, convertToParamMap } from '@angular/router';
import { of, throwError } from 'rxjs';

import { PerfilComponent } from './perfil.component';
import { EstudianteService } from '../services/estudiante.service';

const estudio = { idCarrera: 1, carrera: 'Medicina', idUniversidad: 5, universidad: 'Universidad Uno', siglaUniversidad: 'UU', semestre: 2 };
const ana: any = {
  id: 1, nombre: 'Ana', apellido: 'Prueba', nombreCompleto: 'Ana Prueba', edad: 21, genero: 'FEMENINO', fotoPerfil: null,
  intereses: ['Cine', 'Rock'], redes: [{ idRedSocial: 1, redSocial: 'Instagram', urlPerfil: 'https://instagram.com/ana', alias: '@ana' }],
  estudios: [estudio], totalMeGusta: 3, meGustaDelUsuarioActual: false
};
const luis: any = { ...ana, id: 2, nombre: 'Luis', nombreCompleto: 'Luis Prueba', intereses: [], redes: [] };

describe('PerfilComponent', () => {
  let fixture: ComponentFixture<PerfilComponent>;

  const serviceMock: any = {};
  beforeEach(() => {
    serviceMock.obtenerPorId = () => of(ana);
    serviceMock.listarCompaneros = () => of([luis]);
  });

  const crear = async () => {
    await TestBed.configureTestingModule({
      declarations: [PerfilComponent],
      providers: [
        { provide: EstudianteService, useValue: serviceMock },
        { provide: ActivatedRoute, useValue: { paramMap: of(convertToParamMap({ id: '1' })) } }
      ],
      schemas: [NO_ERRORS_SCHEMA]
    }).compileComponents();
    fixture = TestBed.createComponent(PerfilComponent);
    fixture.detectChanges();
  };

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
  });
});
