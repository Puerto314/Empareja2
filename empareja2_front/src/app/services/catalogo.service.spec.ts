import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';

import { CatalogoService } from './catalogo.service';
import { API_URL } from '../config/api.config';

describe('CatalogoService', () => {
  let service: CatalogoService;
  let http: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({ providers: [provideHttpClient(), provideHttpClientTesting()] });
    service = TestBed.inject(CatalogoService);
    http = TestBed.inject(HttpTestingController);
  });

  afterEach(() => http.verify());

  it('debe pedir las universidades', () => {
    let total = 0;
    service.universidades().subscribe(l => total = l.length);
    const req = http.expectOne(`${API_URL}/catalogos/universidades`);
    expect(req.request.method).toBe('GET');
    req.flush([{ idUniversidad: 1 }, { idUniversidad: 2 }]);
    expect(total).toBe(2);
  });

  it('debe pedir las carreras', () => {
    service.carreras().subscribe();
    http.expectOne(`${API_URL}/catalogos/carreras`).flush([]);
  });
});
