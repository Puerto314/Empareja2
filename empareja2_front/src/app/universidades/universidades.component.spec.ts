import { ComponentFixture, TestBed } from '@angular/core/testing';
import { NO_ERRORS_SCHEMA } from '@angular/core';
import { of } from 'rxjs';

import { UniversidadesComponent } from './universidades.component';
import { EstudianteService } from '../services/estudiante.service';

describe('UniversidadesComponent', () => {
  let fixture: ComponentFixture<UniversidadesComponent>;

  const serviceMock = {
    listarUniversidades: () => of([
      { nombre: 'Universidad Uno', sigla: 'UU', cantidad: 2, vistaPrevia: [] },
      { nombre: 'Universidad Dos', sigla: 'UD', cantidad: 1, vistaPrevia: [] }
    ])
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [UniversidadesComponent],
      providers: [{ provide: EstudianteService, useValue: serviceMock }],
      schemas: [NO_ERRORS_SCHEMA]
    }).compileComponents();

    fixture = TestBed.createComponent(UniversidadesComponent);
    fixture.detectChanges();
  });

  it('debe crearse', () => {
    expect(fixture.componentInstance).toBeTruthy();
  });

  it('debe calcular el total de estudiantes', () => {
    expect(fixture.componentInstance.total()).toBe(3);
  });

  it('debe mostrar una tarjeta por universidad', () => {
    expect(fixture.nativeElement.querySelectorAll('.tile').length).toBe(2);
  });
});
