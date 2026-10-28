import { ComponentFixture, TestBed } from '@angular/core/testing';
import { NO_ERRORS_SCHEMA } from '@angular/core';
import { of, throwError } from 'rxjs';

import { UniversidadesComponent } from './universidades.component';
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
    fixture.detectChanges();
    return fixture;
  };

  it('debe crearse', async () => {
    expect((await crear()).componentInstance).toBeTruthy();
  });

  it('debe mostrar el total de estudiantes que entrega el servicio', async () => {
    expect((await crear()).componentInstance.total()).toBe(3);
  });

  it('debe mostrar una tarjeta por universidad', async () => {
    const fixture = await crear();
    expect(fixture.nativeElement.querySelectorAll('.tile').length).toBe(2);
  });

  it('debe mostrar un mensaje si el servidor falla', async () => {
    serviceMock.listarResumen = () => throwError(() => new Error('falló'));
    const fixture = await crear();
    expect(fixture.nativeElement.querySelector('.error').textContent).toContain('No pudimos cargar las universidades');
    expect(fixture.nativeElement.querySelectorAll('.tile').length).toBe(0);
  });
});
