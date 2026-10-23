import { TestBed } from '@angular/core/testing';

import { EstudianteService } from './estudiante.service';

describe('EstudianteService', () => {
  let service: EstudianteService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(EstudianteService);
  });

  it('debe crearse', () => {
    expect(service).toBeTruthy();
  });

  it('debe listar estudiantes', () => {
    let total = 0;
    service.listarTodos().subscribe(l => total = l.length);
    expect(total).toBeGreaterThan(0);
  });

  it('debe agrupar estudiantes por universidad', () => {
    let sumaGrupos = 0, total = 0;
    service.listarUniversidades().subscribe(us => sumaGrupos = us.reduce((s, u) => s + u.cantidad, 0));
    service.listarTodos().subscribe(l => total = l.length);
    expect(sumaGrupos).toBe(total);
  });

  it('debe devolver compañeros sin incluir al propio estudiante', () => {
    let ids: number[] = [];
    service.listarCompaneros(1).subscribe(l => ids = l.map(e => e.id));
    expect(ids).not.toContain(1);
    expect(ids.length).toBeGreaterThan(0);
  });

  it('debe devolver indefinido si el estudiante no existe', () => {
    let resultado: any = 'pendiente';
    service.obtenerPorId(9999).subscribe(e => resultado = e);
    expect(resultado).toBeUndefined();
  });
});
