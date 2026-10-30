import { ComponentFixture, TestBed } from '@angular/core/testing';
import { NO_ERRORS_SCHEMA } from '@angular/core';
import { ActivatedRoute, convertToParamMap } from '@angular/router';
import { of, throwError } from 'rxjs';

import { EstudiantesComponent } from './estudiantes.component';
import { EstudianteService } from '../services/estudiante.service';
import { UniversidadService } from '../services/universidad.service';

const alumno = (id: number, nombreCompleto: string, carrera: string, semestre: number) => ({
  id, nombre: nombreCompleto, apellido: '', nombreCompleto, edad: 20, genero: null, fotoPerfil: null,
  intereses: [], redes: [], totalMeGusta: 0, meGustaDelUsuarioActual: false,
  estudios: [{ idCarrera: 1, carrera, idUniversidad: 5, universidad: 'Universidad Uno', siglaUniversidad: 'UU', semestre }]
});

describe('EstudiantesComponent', () => {
  let component: EstudiantesComponent;
  let fixture: ComponentFixture<EstudiantesComponent>;
  let idPedido = 0;

  const universidadMock: any = {
    obtener: (id: number) => of(id === 5
      ? { idUniversidad: 5, nombre: 'Universidad Uno', sigla: 'UU', calle: null, barrio: null, numeroDireccion: null, ciudad: null }
      : undefined)
  };
  const estudianteMock: any = {};

  const crear = async (id: string) => {
    await TestBed.configureTestingModule({
      declarations: [EstudiantesComponent],
      providers: [
        { provide: EstudianteService, useValue: estudianteMock },
        { provide: UniversidadService, useValue: universidadMock },
        { provide: ActivatedRoute, useValue: { paramMap: of(convertToParamMap({ id })) } }
      ],
      schemas: [NO_ERRORS_SCHEMA]
    }).compileComponents();
    fixture = TestBed.createComponent(EstudiantesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  };

  beforeEach(() => {
    idPedido = 0;
    estudianteMock.listarPorUniversidad = (id: number) => {
      idPedido = id;
      return of([alumno(1, 'Ana Prueba', 'Medicina', 2), alumno(2, 'Luis Prueba', 'Derecho', 8)]);
    };
  });

  it('debe crearse', async () => {
    await crear('5');
    expect(component).toBeTruthy();
  });

  it('debe cargar la universidad y sus estudiantes usando el id de la ruta', async () => {
    await crear('5');
    expect(component.uni()).toBe('Universidad Uno');
    expect(idPedido).toBe(5);
  });

  it('debe mostrar una tarjeta por estudiante con su carrera y semestre en esa universidad', async () => {
    await crear('5');
    const tarjetas = fixture.nativeElement.querySelectorAll('.card');
    expect(tarjetas.length).toBe(2);
    expect(tarjetas[0].textContent).toContain('Medicina');
    expect(tarjetas[0].textContent).toContain('Semestre 2');
  });

  it('debe filtrar por texto de búsqueda', async () => {
    await crear('5');
    component.q.set('medicina');
    expect(component.list().length).toBe(1);
  });

  it('debe filtrar por rango de semestre', async () => {
    await crear('5');
    component.rango.set('c');
    expect(component.list().map(s => s.id)).toEqual([2]);
  });

  it('debe avisar si la universidad no existe', async () => {
    await crear('999');
    expect(fixture.nativeElement.textContent).toContain('No encontramos esta universidad');
    expect(idPedido).toBe(0);
  });

  it('debe mostrar un mensaje si el servidor falla', async () => {
    estudianteMock.listarPorUniversidad = () => throwError(() => new Error('falló'));
    await crear('5');
    expect(fixture.nativeElement.querySelector('[role=alert]').textContent).toContain('No pudimos cargar los estudiantes');
  });
});
