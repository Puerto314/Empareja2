import { ComponentFixture, TestBed } from '@angular/core/testing';
import { NO_ERRORS_SCHEMA } from '@angular/core';
import { ActivatedRoute, convertToParamMap } from '@angular/router';
import { of } from 'rxjs';

import { PerfilComponent } from './perfil.component';
import { EstudianteService } from '../services/estudiante.service';

describe('PerfilComponent', () => {
  let fixture: ComponentFixture<PerfilComponent>;

  const serviceMock = {
    obtenerPorId: () => of({ id: 1, fullName: 'Ana Prueba', career: 'Medicina', semester: 2, university: 'Universidad Uno' }),
    listarCompaneros: () => of([
      { id: 2, fullName: 'Luis Prueba', career: 'Derecho', semester: 8, university: 'Universidad Uno' }
    ])
  };
  const routeMock = { paramMap: of(convertToParamMap({ id: '1' })) };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [PerfilComponent],
      providers: [
        { provide: EstudianteService, useValue: serviceMock },
        { provide: ActivatedRoute, useValue: routeMock }
      ],
      schemas: [NO_ERRORS_SCHEMA]
    }).compileComponents();

    fixture = TestBed.createComponent(PerfilComponent);
    fixture.detectChanges();
  });

  it('debe crearse', () => {
    expect(fixture.componentInstance).toBeTruthy();
  });

  it('debe mostrar el nombre del estudiante', () => {
    expect(fixture.nativeElement.querySelector('h1').textContent).toContain('Ana Prueba');
  });

  it('debe listar compañeros de la misma universidad', () => {
    expect(fixture.nativeElement.querySelectorAll('.mini-row').length).toBe(1);
  });
});
