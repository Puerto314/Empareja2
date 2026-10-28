import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';

import { EstudianteService } from './estudiante.service';
import { API_URL } from '../config/api.config';
import { EstudianteModel } from '../models/estudiante.model';

const estudiante = (id: number, idUniversidad = 1): EstudianteModel => ({
  id, nombre: 'N' + id, apellido: 'A', nombreCompleto: `N${id} A`, edad: 20, genero: null, fotoPerfil: null,
  intereses: [], redes: [], totalMeGusta: 0, meGustaDelUsuarioActual: false,
  estudios: [{ idCarrera: 1, carrera: 'Medicina', idUniversidad, universidad: 'Uni', siglaUniversidad: 'U', semestre: 2 }]
});

describe('EstudianteService', () => {
  let service: EstudianteService;
  let http: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({ providers: [provideHttpClient(), provideHttpClientTesting()] });
    service = TestBed.inject(EstudianteService);
    http = TestBed.inject(HttpTestingController);
  });

  afterEach(() => http.verify());

  it('debe crearse', () => {
    expect(service).toBeTruthy();
  });

  it('debe listar estudiantes con GET /usuarios', () => {
    let total = 0;
    service.listarTodos().subscribe(l => total = l.length);
    http.expectOne(`${API_URL}/usuarios`).flush([estudiante(1), estudiante(2)]);
    expect(total).toBe(2);
  });

  it('debe filtrar por universidad con ?universidadId', () => {
    service.listarPorUniversidad(7).subscribe();
    const req = http.expectOne(r => r.url === `${API_URL}/usuarios`);
    expect(req.request.params.get('universidadId')).toBe('7');
    req.flush([]);
  });

  it('debe obtener un estudiante por id', () => {
    let nombre = '';
    service.obtenerPorId(3).subscribe(e => nombre = e?.nombreCompleto ?? '');
    http.expectOne(`${API_URL}/usuarios/3`).flush(estudiante(3));
    expect(nombre).toBe('N3 A');
  });

  it('debe devolver indefinido si el estudiante no existe (404)', () => {
    let resultado: EstudianteModel | undefined | 'pendiente' = 'pendiente';
    service.obtenerPorId(9999).subscribe(e => resultado = e);
    http.expectOne(`${API_URL}/usuarios/9999`).flush({ message: 'no existe' }, { status: 404, statusText: 'Not Found' });
    expect(resultado).toBeUndefined();
  });

  it('debe propagar otros errores (500)', () => {
    let fallo = false;
    service.obtenerPorId(1).subscribe({ error: () => fallo = true });
    http.expectOne(`${API_URL}/usuarios/1`).flush(null, { status: 500, statusText: 'Error' });
    expect(fallo).toBe(true);
  });

  it('debe devolver compañeros sin incluir al propio estudiante y con límite', () => {
    let ids: number[] = [];
    service.listarCompaneros(estudiante(1), 2).subscribe(l => ids = l.map(e => e.id));
    http.expectOne(r => r.url === `${API_URL}/usuarios`).flush([1, 2, 3, 4].map(i => estudiante(i)));
    expect(ids).toEqual([2, 3]);
  });

  it('no debe pedir compañeros si el estudiante no tiene estudios', () => {
    let ids: number[] = [-1]; // -1 = el observable aún no emitió
    service.listarCompaneros({ ...estudiante(1), estudios: [] }).subscribe(l => ids = l.map(e => e.id));
    expect(ids).toEqual([]);
  });
});
