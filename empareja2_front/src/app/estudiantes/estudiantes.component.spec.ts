import { ComponentFixture, TestBed } from '@angular/core/testing';
import { NO_ERRORS_SCHEMA } from '@angular/core';
import { ActivatedRoute, convertToParamMap } from '@angular/router';
import { of } from 'rxjs';

import { EstudiantesComponent } from './estudiantes.component';
import { EstudianteService } from '../services/estudiante.service';

describe('EstudiantesComponent', () => {
  let component: EstudiantesComponent;
  let fixture: ComponentFixture<EstudiantesComponent>;

  const serviceMock = {
    listarPorUniversidad: () => of([
      { id: 1, fullName: 'Ana Prueba', career: 'Medicina', semester: 2, university: 'Universidad Uno' },
      { id: 2, fullName: 'Luis Prueba', career: 'Derecho', semester: 8, university: 'Universidad Uno' }
    ])
  };
  const routeMock = { paramMap: of(convertToParamMap({ id: 'Universidad Uno' })) };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [EstudiantesComponent],
      providers: [
        { provide: EstudianteService, useValue: serviceMock },
        { provide: ActivatedRoute, useValue: routeMock }
      ],
      schemas: [NO_ERRORS_SCHEMA]
    }).compileComponents();

    fixture = TestBed.createComponent(EstudiantesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('debe crearse', () => {
    expect(component).toBeTruthy();
  });

  it('debe leer la universidad desde la ruta', () => {
    expect(component.uni()).toBe('Universidad Uno');
  });

  it('debe mostrar una tarjeta por estudiante', () => {
    expect(fixture.nativeElement.querySelectorAll('.card').length).toBe(2);
  });

  it('debe filtrar por texto de búsqueda', () => {
    component.q.set('medicina');
    expect(component.list().length).toBe(1);
  });

  it('debe filtrar por rango de semestre', () => {
    component.rango.set('c');
    expect(component.list().map(s => s.id)).toEqual([2]);
  });
});
