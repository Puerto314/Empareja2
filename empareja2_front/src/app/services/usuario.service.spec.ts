import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';

import { UsuarioService } from './usuario.service';
import { API_URL } from '../config/api.config';

describe('UsuarioService', () => {
  let service: UsuarioService;
  let http: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({ providers: [provideHttpClient(), provideHttpClientTesting()] });
    service = TestBed.inject(UsuarioService);
    http = TestBed.inject(HttpTestingController);
  });

  afterEach(() => http.verify());

  it('debe listar todos los estudiantes sin filtro', () => {
    service.listarCatalogo().subscribe();
    const req = http.expectOne(r => r.url === `${API_URL}/usuarios`);
    expect(req.request.params.has('universidadId')).toBe(false);
    req.flush([]);
  });

  it('debe filtrar por universidad con el parámetro universidadId', () => {
    service.listarCatalogo(3).subscribe();
    const req = http.expectOne(r => r.url === `${API_URL}/usuarios`);
    expect(req.request.params.get('universidadId')).toBe('3');
    req.flush([]);
  });

  it('debe obtener el perfil público por id', () => {
    service.obtenerPublico(5).subscribe();
    const req = http.expectOne(`${API_URL}/usuarios/5`);
    expect(req.request.method).toBe('GET');
    req.flush({});
  });

  it('debe dar y quitar me gusta', () => {
    service.darMeGusta(5).subscribe();
    expect(http.expectOne(`${API_URL}/usuarios/5/me-gusta`).request.method).toBe('POST');
    service.quitarMeGusta(5).subscribe();
    expect(http.match(`${API_URL}/usuarios/5/me-gusta`)[0].request.method).toBe('DELETE');
  });
});
