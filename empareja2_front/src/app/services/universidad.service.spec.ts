import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';

import { UniversidadService } from './universidad.service';
import { API_URL } from '../config/api.config';
import { ResumenUniversidadesModel, UniversidadModel } from '../models/universidad.model';

const uni = (idUniversidad: number, nombre: string): UniversidadModel => ({
  idUniversidad, nombre, sigla: nombre.slice(0, 3).toUpperCase(), calle: null, barrio: null, numeroDireccion: null, ciudad: null
});
const est = (id: number, ...unis: number[]) => ({
  id, nombreCompleto: 'E' + id,
  estudios: unis.map(u => ({ idCarrera: 1, carrera: 'C', idUniversidad: u, universidad: 'U', siglaUniversidad: 'U', semestre: 1 }))
});

describe('UniversidadService', () => {
  let service: UniversidadService;
  let http: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({ providers: [provideHttpClient(), provideHttpClientTesting()] });
    service = TestBed.inject(UniversidadService);
    http = TestBed.inject(HttpTestingController);
  });

  afterEach(() => http.verify());

  it('debe listar universidades con GET /catalogos/universidades', () => {
    let total = 0;
    service.listar().subscribe(l => total = l.length);
    http.expectOne(`${API_URL}/catalogos/universidades`).flush([uni(1, 'Uno'), uni(2, 'Dos')]);
    expect(total).toBe(2);
  });

  it('debe devolver indefinido si la universidad no existe (404)', () => {
    let resultado: UniversidadModel | undefined | 'pendiente' = 'pendiente';
    service.obtener(99).subscribe(u => resultado = u);
    http.expectOne(`${API_URL}/catalogos/universidades/99`).flush(null, { status: 404, statusText: 'Not Found' });
    expect(resultado).toBeUndefined();
  });

  it('debe armar el resumen: cuenta estudiantes, ordena por nombre e incluye universidades vacías', () => {
    let resumen = [] as ResumenUniversidadesModel['universidades'];
    let total = 0;
    service.listarResumen().subscribe(r => { resumen = r.universidades; total = r.totalEstudiantes; });
    http.expectOne(`${API_URL}/catalogos/universidades`).flush([uni(2, 'Zeta'), uni(1, 'Alfa'), uni(3, 'Vacía')]);
    http.expectOne(`${API_URL}/usuarios`).flush([est(10, 1), est(11, 1, 2), est(12, 2)]);

    expect(resumen.map(u => u.nombre)).toEqual(['Alfa', 'Vacía', 'Zeta']);
    expect(resumen.map(u => u.cantidad)).toEqual([2, 0, 2]);
    expect(resumen[0].vistaPrevia.map(e => e.id)).toEqual([10, 11]);
    // El estudiante 11 estudia en dos universidades pero es una sola persona: 3 estudiantes, no 4.
    expect(total).toBe(3);
  });

  it('debe limitar la vista previa a 4 estudiantes', () => {
    let resumen = [] as ResumenUniversidadesModel['universidades'];
    service.listarResumen().subscribe(r => resumen = r.universidades);
    http.expectOne(`${API_URL}/catalogos/universidades`).flush([uni(1, 'Alfa')]);
    http.expectOne(`${API_URL}/usuarios`).flush([1, 2, 3, 4, 5, 6].map(i => est(i, 1)));
    expect(resumen[0].cantidad).toBe(6);
    expect(resumen[0].vistaPrevia.length).toBe(4);
  });
});
